package com.amdcruz.alertarotulo.product.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amdcruz.alertarotulo.history.data.HistoryRepository
import com.amdcruz.alertarotulo.history.data.mapper.HistoryMapper
import com.amdcruz.alertarotulo.product.data.ProductRepository
import com.amdcruz.alertarotulo.product.data.model.ProductLookupResult
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val historyRepository: HistoryRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProductUiState>(ProductUiState.Idle)
    val uiState: StateFlow<ProductUiState> = _uiState.asStateFlow()

    private var lastRequestedCode: String? = null
    private var openedFromHistory: Boolean = false

    fun lookup(code: String) {
        openedFromHistory = false
        val normalizedCode = code.filter { it.isDigit() }
        if (normalizedCode.isEmpty()) {
            _uiState.value = ProductUiState.NotFound(code = code)
            return
        }
        lastRequestedCode = normalizedCode
        viewModelScope.launch {
            _uiState.value = ProductUiState.Loading
            _uiState.value = when (val result = productRepository.lookup(normalizedCode)) {
                is ProductLookupResult.Success -> {
                    historyRepository.saveOrUpdate(result.product)
                    ProductUiState.Success(result.product)
                }
                ProductLookupResult.NotFound -> ProductUiState.NotFound(normalizedCode)
                ProductLookupResult.NetworkError -> ProductUiState.NetworkError(normalizedCode)
            }
        }
    }

    fun openSnapshot(code: String) {
        openedFromHistory = true
        val normalizedCode = code.filter { it.isDigit() }
        lastRequestedCode = normalizedCode
        viewModelScope.launch {
            val historyItem = historyRepository.getByCode(normalizedCode)
            _uiState.value = if (historyItem == null) {
                ProductUiState.NotFound(normalizedCode)
            } else {
                ProductUiState.Success(HistoryMapper.toProduct(historyItem))
            }
        }
    }

    fun retry() {
        if (openedFromHistory) {
            return
        }
        val code = lastRequestedCode ?: return
        lookup(code)
    }
}

package com.amdcruz.alertarotulo.product.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProductUiState>(ProductUiState.Idle)
    val uiState: StateFlow<ProductUiState> = _uiState.asStateFlow()

    private var lastRequestedCode: String? = null

    fun lookup(code: String) {
        val normalizedCode = code.filter { it.isDigit() }
        if (normalizedCode.isEmpty()) {
            _uiState.value = ProductUiState.NotFound(code = code)
            return
        }
        lastRequestedCode = normalizedCode
        viewModelScope.launch {
            _uiState.value = ProductUiState.Loading
            _uiState.value = when (val result = productRepository.lookup(normalizedCode)) {
                is ProductLookupResult.Success -> ProductUiState.Success(result.product)
                ProductLookupResult.NotFound -> ProductUiState.NotFound(normalizedCode)
                ProductLookupResult.NetworkError -> ProductUiState.NetworkError(normalizedCode)
            }
        }
    }

    fun retry() {
        val code = lastRequestedCode ?: return
        lookup(code)
    }
}

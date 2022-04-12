package lol.xget.groceryapp.mainSeller.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.navigationBarsWithImePadding
import lol.xget.groceryapp.mainSeller.domain.ProductModel


@ExperimentalMaterialApi
@Composable
fun ModalBottomSheet( sheetState : ModalBottomSheetState, productModel: ProductModel?) {
    val coroutineScope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        sheetContent = {
            Box(
                modifier = Modifier
                    .navigationBarsWithImePadding()
                    .height(200.dp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.onSurface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp)
                ) {
                    productModel?.productTitle?.let {
                        Text(
                            text = it,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.h5,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Text(
                        text = "Item 1",
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                    Text(
                        text = "Item 2",
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                    Text(
                        text = "Item 3",
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                }
            }
        },
        sheetState = sheetState,
        sheetBackgroundColor = MaterialTheme.colors.primaryVariant
    ) {

    }
}

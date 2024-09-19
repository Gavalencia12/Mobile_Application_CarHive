package com.example.carhive

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter

@Composable
fun <T> CarScreen(viewModel: CarViewModel = viewModel()) {
    var carName by remember { mutableStateOf("") }
    val imageUris by viewModel.imageUris.collectAsState()
    val context = LocalContext.current

    // Image picker launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            if (imageUris.size < 5) {
                viewModel.addImageUri(it)
            } else {
                Toast.makeText(context, context.getString(R.string.max_images_allowed), Toast.LENGTH_SHORT).show()
            }
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        // Car name input field
        TextField(
            value = carName,
            onValueChange = { carName = it },
            label = { Text(stringResource(R.string.car_name_label)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Button to select image
        Button(onClick = { imagePickerLauncher.launch("image/*") }, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.select_image))
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Display selected images in a LazyColumn
        LazyColumn {
            items(imageUris) { uri ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painter = rememberAsyncImagePainter(uri),
                        contentDescription = stringResource(R.string.image_preview_description),
                        modifier = Modifier.size(100.dp),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { viewModel.removeImageUri(uri) }) {
                        Text(stringResource(R.string.remove_image))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Create car button
        Button(
            onClick = {
                if (imageUris.size == 5) {
                    viewModel.createCar(carName)
                    carName = ""
                } else {
                    Toast.makeText(context, context.getString(R.string.need_five_images), Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.create_car))
        }
    }
}

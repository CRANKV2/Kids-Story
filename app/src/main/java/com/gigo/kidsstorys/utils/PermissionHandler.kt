package com.gigo.kidsstorys.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.core.content.ContextCompat
import com.gigo.kidsstorys.ui.components.PermissionDialog
import com.gigo.kidsstorys.ui.components.ThankYouDialog

@Composable
fun RequestStoragePermission(
    context: Context,
    onPermissionGranted: () -> Unit = {},
    onPermissionDenied: () -> Unit = {},
    onExit: () -> Unit = {}
) {
    var showPermissionDialog by remember { mutableStateOf(true) }
    var showThankYouDialog by remember { mutableStateOf(false) }

    val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_VIDEO,
            Manifest.permission.READ_MEDIA_AUDIO
        )
    } else {
        arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsMap ->
        val allGranted = permissionsMap.values.all { it }
        if (allGranted) {
            showThankYouDialog = true
            onPermissionGranted()
        } else {
            onExit()
        }
        showPermissionDialog = false
    }

    if (showPermissionDialog) {
        val allGranted = permissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
        
        if (!allGranted) {
            PermissionDialog(
                onDismiss = { 
                    showPermissionDialog = false
                    onExit()
                },
                onConfirm = { launcher.launch(permissions) }
            )
        }
    }

    if (showThankYouDialog) {
        ThankYouDialog(
            onDismiss = {
                showThankYouDialog = false
                onPermissionGranted()
            }
        )
    }
} 
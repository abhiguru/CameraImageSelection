package `in`.tutorial.cameraimageselection

import android.app.Activity
import android.app.Instrumentation.ActivityResult
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import `in`.tutorial.cameraimageselection.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    companion object{
        private const val CAM_PERM_CODE = 1
        private const val CAM = 2
    }
    var binding : ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        binding?.btnCamera?.setOnClickListener {
            if(ContextCompat.checkSelfPermission(
                    this, android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED){
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                //resultLauncher.launch(intent, CAM)
                startActivityForResult(intent, CAM)
            }else{
                ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.CAMERA), CAM_PERM_CODE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == CAM){
                val thumb:Bitmap = data!!.extras!!.get("data") as Bitmap
                binding?.ivCam!!.setImageBitmap(thumb)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == CAM_PERM_CODE){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, CAM)
            }else{
                Toast.makeText(this@MainActivity,
                    "Camera denied", Toast.LENGTH_LONG).show()
            }
        }
    }

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result ->{
            val data: Intent? = result.data
            if(data!=null){
                Toast.makeText(this@MainActivity,
                    "Camera soon", Toast.LENGTH_LONG).show()
            }
        }
    }
}
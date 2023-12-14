package capstone.catora.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import capstone.catora.data.ResultState
import capstone.catora.data.pref.UserModel
import capstone.catora.databinding.ActivityLoginBinding
import capstone.catora.ui.ViewModelFactory
import capstone.catora.ui.main.MainActivity
import capstone.catora.ui.register.RegisterActivity
import capstone.catora.ui.register.RegisterViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide() //this line for remove action bar

        binding.tvToRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            //handling the login logic
        }

        setupAction()
    }

    private fun setupAction() {
        binding.btnLogin.setOnClickListener {
            val email = binding.textInput.text.toString()
            val password = binding.textPassword.text.toString()

            viewModel.userLogin(email, password).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is ResultState.Loading -> {
                            showLoading(true)
                        }
                        is ResultState.Success -> {
                            val message = result.data.message
                            showSuccessDialog(message)


                            viewModel.saveSession(UserModel(email))


                            showLoading(false)
                        }
                        is ResultState.Error -> {
                            val error = result.error
                            showToast(error)
                            showLoading(false)

                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()
                        }
                    }
                }
            }
        }
    }

    private fun showSuccessDialog(message: String) {
        AlertDialog.Builder(this).apply {
            setTitle("Yeah!")
            setMessage(
                "welcome to our app ${binding.textInput.text.toString()}"
            )
            setPositiveButton("continue") { _, _ ->
                val intent = Intent(context, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            create()
            show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
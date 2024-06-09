package com.chaitanya.mgrecipe.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.chaitanya.mgrecipe.R
import com.chaitanya.mgrecipe.databinding.FragmentLoginBinding
import com.chaitanya.mgrecipe.utility.SharedPreference
import com.chaitanya.mgrecipe.utility.gone
import com.chaitanya.mgrecipe.utility.visible
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException


class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentLoginBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val window = activity?.window
        window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window?.statusBarColor = resources.getColor(R.color.black)


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)


        binding.btnGoogleLogin.setOnClickListener {
            val signInIntent = mGoogleSignInClient.signInIntent
            binding.pbLoad.visible()
            activityResult.launch(signInIntent)
        }
    }
    private val activityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {

                val account = task.getResult(ApiException::class.java)
                SharedPreference.setUserName(account.displayName)
                SharedPreference.setLoggedIn(true)
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment2)
            } catch (e: ApiException) {
            }catch (_:Exception){}

        }
        binding.pbLoad.gone()
    }
}
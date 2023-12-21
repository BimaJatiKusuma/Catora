package capstone.catora.ui.main.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import capstone.catora.R
import capstone.catora.data.pref.UserPreferences
import capstone.catora.data.pref.dataStore
import capstone.catora.data.remote.api.response.AllArtworkResponseItem
import capstone.catora.databinding.FragmentProfileBinding
import capstone.catora.ui.ViewModelFactory
import capstone.catora.ui.WelcomeActivity
import capstone.catora.ui.adapter.AllArtAdapter
import capstone.catora.ui.editprofile.EditProfileActivity
import capstone.catora.ui.main.MainActivity
import capstone.catora.ui.orderprocess.OrderProcessActivity
import com.bumptech.glide.Glide
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        profileViewModel = ViewModelProvider(this)[ProfileViewModel::class.java]

        profileViewModel = ViewModelProvider(this, ViewModelFactory.getInstance(requireContext())).get(ProfileViewModel::class.java)


        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val pref = UserPreferences.getInstance(requireActivity().dataStore)
        val user = runBlocking { pref.getSession().first() }
        val userId = user.userId

        Log.d("userid", userId)

        profileViewModel.getUserByID(userId)
        profileViewModel.userId.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.tvUsername.text = it.artistName
                binding.tvProfileDescription.text = it.description

                if (it.profileImageUrl!= null){
                    Glide.with(requireActivity())
                        .load(it.profileImageUrl)
                        .circleCrop()
                        .into(binding.ivProfileImage)
                }
            }
        }

        profileViewModel.getArtworkById(userId)
        profileViewModel.listArtwork.observe(viewLifecycleOwner) {
            if (it != null ) {
                binding.tvNoArtwork.visibility = View.GONE
                setAllArtwork(it)
            }
        }

//        binding.rvArtworkProfile.setHasFixedSize(true)
        binding.rvArtworkProfile.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        binding.btnMyOrder.setOnClickListener {
            startActivity(Intent(requireContext(), OrderProcessActivity::class.java))
        }

        binding.btnEditProfile.setOnClickListener {
            val moveData = Intent(requireActivity(), EditProfileActivity::class.java)
            moveData.putExtra("userid", userId)
            requireActivity().startActivity(moveData)
        }

        val toolbar: Toolbar = root.findViewById(R.id.toolbar)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

        setHasOptionsMenu(true)
        return root
    }

    private fun setAllArtwork(artwork: List<AllArtworkResponseItem>?) {
        val adapter = AllArtAdapter()
        adapter.submitList(artwork)
        binding.rvArtworkProfile.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//    companion object {
//        fun newInstance(userId: String): ProfileFragment {
//            val fragment = ProfileFragment()
//            val args = Bundle()
//            args.putString("userId", userId)
//            fragment.arguments = args
//            return fragment
//        }
//    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        // Inflate the menu resource file
        inflater.inflate(R.menu.menu_profile, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_logout -> {
                profileViewModel.logout()
                val intent = Intent(requireContext(), WelcomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                return true
            }
            // Handle other menu items if needed

            else -> return super.onOptionsItemSelected(item)
        }
    }
}
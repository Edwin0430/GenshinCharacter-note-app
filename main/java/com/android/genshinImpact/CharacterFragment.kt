package com.android.genshinImpact

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


private const val ARG_CHARACTER_ID = "character_id"
private const val DIALOG_DATE = "DialogDate"
private const val DIALOG_TIME = "DialogTime"
private const val REQUEST_DATE = 0
private const val REQUEST_TIME = 3
private const val REQUEST_CONTACT = 1
private const val REQUEST_PHOTO = 2
private const val DATE_FORMAT = "EEE, MMM dd"
private const val TIME_FORMAT = "hh:mm a"

class CharacterFragment : Fragment(), DatePickerFragment.Callbacks, TimePickerFragment.Callbacks {
    private lateinit var character: Character
    private lateinit var photoFile: File
    private lateinit var photoUri: Uri
    private lateinit var titleField: EditText
    private lateinit var dateButton: Button
    private lateinit var timeButton: Button
    private lateinit var pyroCheckBox: CheckBox
    private lateinit var hydroCheckBox: CheckBox
    private lateinit var electroCheckBox: CheckBox
    private lateinit var geoCheckBox: CheckBox
    private lateinit var anemoCheckBox: CheckBox
    private lateinit var cryoCheckBox: CheckBox
    private lateinit var dendroCheckBox: CheckBox
    private lateinit var reportButton: Button
    private lateinit var ownerButton: Button
    private lateinit var photoButton: ImageButton
    private lateinit var photoView: ImageView
    private lateinit var deleteButton: Button

    private val characterDetailViewModel: CharacterDetailViewModel by lazy {
        ViewModelProviders.of(this).get(CharacterDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        character = Character()

        val characterId: UUID = arguments?.getSerializable(ARG_CHARACTER_ID) as UUID
        characterDetailViewModel.loadCharacter(characterId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_character, container, false)

        titleField = view.findViewById(R.id.character_title)
        dateButton = view.findViewById(R.id.character_date)
        timeButton = view.findViewById(R.id.character_time)
        pyroCheckBox = view.findViewById(R.id.character_pyro)
        hydroCheckBox = view.findViewById(R.id.character_hydro)
        electroCheckBox = view.findViewById(R.id.character_electro)
        geoCheckBox = view.findViewById(R.id.character_geo)
        anemoCheckBox = view.findViewById(R.id.character_anemo)
        cryoCheckBox = view.findViewById(R.id.character_cryo)
        dendroCheckBox = view.findViewById(R.id.character_dendro)
        reportButton = view.findViewById(R.id.character_report)
        ownerButton = view.findViewById(R.id.character_owner)
        photoButton = view.findViewById(R.id.character_camera)
        photoView = view.findViewById(R.id.character_photo)
        deleteButton = view.findViewById(R.id.delete_character_button)

        // Set up CheckBox listeners
        pyroCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // If the Pyro checkbox is checked, uncheck all other checkboxes
                hydroCheckBox.isChecked = false
                electroCheckBox.isChecked = false
                geoCheckBox.isChecked = false
                anemoCheckBox.isChecked = false
                cryoCheckBox.isChecked = false
                dendroCheckBox.isChecked = false
            }
            character.isPyro = isChecked
        }
        hydroCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // If the Hydro checkbox is checked, uncheck all other checkboxes
                pyroCheckBox.isChecked = false
                electroCheckBox.isChecked = false
                geoCheckBox.isChecked = false
                anemoCheckBox.isChecked = false
                cryoCheckBox.isChecked = false
                dendroCheckBox.isChecked = false
            }
            character.isHydro = isChecked
        }
        electroCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // If the Electro checkbox is checked, uncheck all other checkboxes
                pyroCheckBox.isChecked = false
                hydroCheckBox.isChecked = false
                geoCheckBox.isChecked = false
                anemoCheckBox.isChecked = false
                cryoCheckBox.isChecked = false
                dendroCheckBox.isChecked = false
            }
            character.isElectro = isChecked
        }
        geoCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // If the Geo checkbox is checked, uncheck all other checkboxes
                pyroCheckBox.isChecked = false
                hydroCheckBox.isChecked = false
                electroCheckBox.isChecked = false
                anemoCheckBox.isChecked = false
                cryoCheckBox.isChecked = false
                dendroCheckBox.isChecked = false
            }
            character.isGeo = isChecked
        }
        anemoCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // If the Anemo checkbox is checked, uncheck all other checkboxes
                pyroCheckBox.isChecked = false
                hydroCheckBox.isChecked = false
                electroCheckBox.isChecked = false
                geoCheckBox.isChecked = false
                cryoCheckBox.isChecked = false
                dendroCheckBox.isChecked = false
            }
            character.isAnemo = isChecked
        }
        cryoCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // If the Cryo checkbox is checked, uncheck all other checkboxes
                pyroCheckBox.isChecked = false
                hydroCheckBox.isChecked = false
                electroCheckBox.isChecked = false
                geoCheckBox.isChecked = false
                anemoCheckBox.isChecked = false
                dendroCheckBox.isChecked = false
            }
            character.isCryo = isChecked
        }
        dendroCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // If the Dendro checkbox is checked, uncheck all other checkboxes
                pyroCheckBox.isChecked = false
                hydroCheckBox.isChecked = false
                electroCheckBox.isChecked = false
                geoCheckBox.isChecked = false
                anemoCheckBox.isChecked = false
                cryoCheckBox.isChecked = false
            }
            character.isDendro = isChecked
        }
        deleteButton = view.findViewById(R.id.delete_character_button)
        deleteButton.setOnClickListener {
            // Call the deleteCharacter function from the ViewModel or Repository
            characterDetailViewModel.deleteCharacter(character)
        }
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        characterDetailViewModel.characterLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer { character ->
            character?.let {
                this.character = character
                photoFile = characterDetailViewModel.getPhotoFile(character)
                photoUri = FileProvider.getUriForFile(
                    requireActivity(),
                    "com.android.genshinImpact.fileprovider",
                    photoFile
                )
                updateUI()
            }
        })
    }

    override fun onStart() {
        super.onStart()

        // Add TextWatcher for title EditText
        val titleWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Intentionally blank
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                character.title = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                // Intentionally blank
            }
        }
        titleField.addTextChangedListener(titleWatcher)

        // Set up click listeners for buttons
        dateButton.setOnClickListener {
            DatePickerFragment.newInstance(character.date).apply {
                setTargetFragment(this@CharacterFragment, REQUEST_DATE)
                show(this@CharacterFragment.requireFragmentManager(), DIALOG_DATE)
            }
        }

        timeButton.setOnClickListener {
            TimePickerFragment.newInstance(character.time).apply {
                setTargetFragment(this@CharacterFragment, REQUEST_TIME)
                show(this@CharacterFragment.requireFragmentManager(), DIALOG_TIME)
            }
        }

        reportButton.setOnClickListener {
            Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, getCharacterReport())
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.character_report_subject))
            }.also { intent ->
                val chooserIntent = Intent.createChooser(intent, getString(R.string.send_report))
                startActivity(chooserIntent)
            }
        }

        ownerButton.setOnClickListener {
            val pickContactIntent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
            startActivityForResult(pickContactIntent, REQUEST_CONTACT)
        }

        photoButton.setOnClickListener {
            val packageManager: PackageManager = requireActivity().packageManager
            val captureImage = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val resolvedActivity: ResolveInfo? =
                packageManager.resolveActivity(captureImage, PackageManager.MATCH_DEFAULT_ONLY)
            if (resolvedActivity == null) {
                photoButton.isEnabled = false
            } else {
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                val cameraActivities: List<ResolveInfo> =
                    packageManager.queryIntentActivities(captureImage, PackageManager.MATCH_DEFAULT_ONLY)
                for (cameraActivity in cameraActivities) {
                    requireActivity().grantUriPermission(
                        cameraActivity.activityInfo.packageName,
                        photoUri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    )
                }
                startActivityForResult(captureImage, REQUEST_PHOTO)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        characterDetailViewModel.saveCharacter(character)
    }

    override fun onDetach() {
        super.onDetach()
        requireActivity().revokeUriPermission(photoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
    }

    override fun onDateSelected(date: Date) {
        character.date = date
        updateUI()
    }

    override fun onTimeSelected(hourOfDay: Int, minute: Int) {
        character.time = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hourOfDay)
            set(Calendar.MINUTE, minute)
        }.time
        updateTimeButton(character.time)
    }

    private fun updateTimeButton(time: Date) {
        val formattedTime = SimpleDateFormat(TIME_FORMAT, Locale.getDefault()).format(time)
        timeButton.text = formattedTime
    }

    private fun updateUI() {
        titleField.setText(character.title)
        dateButton.text = DateFormat.format(DATE_FORMAT, character.date).toString()
        updateTimeButton(character.time)
        pyroCheckBox.isChecked = character.isPyro
        hydroCheckBox.isChecked = character.isHydro
        electroCheckBox.isChecked = character.isElectro
        geoCheckBox.isChecked = character.isGeo
        anemoCheckBox.isChecked = character.isAnemo
        cryoCheckBox.isChecked = character.isCryo
        dendroCheckBox.isChecked = character.isDendro
        updatePhotoView()
    }

    private fun updatePhotoView() {
        if (photoFile.exists()) {
            val bitmap = getScaledBitmap(photoFile.path, requireActivity())
            photoView.setImageBitmap(bitmap)
            photoView.contentDescription = getString(R.string.character_photo_image_description)
        } else {
            photoView.setImageDrawable(null)
            photoView.contentDescription = getString(R.string.character_photo_no_image_description)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when {
            resultCode != Activity.RESULT_OK -> return
            requestCode == REQUEST_CONTACT && data != null -> {
                val contactUri: Uri? = data.data
                val queryFields = arrayOf(ContactsContract.Contacts.DISPLAY_NAME)
                val cursor = contactUri?.let {
                    requireActivity().contentResolver.query(it, queryFields, null, null, null)
                }
                cursor?.use {
                    if (it.count == 0) {
                        return
                    }
                    it.moveToFirst()
                    val owner = it.getString(0)
                    character.owner = owner
                    characterDetailViewModel.saveCharacter(character)
                    ownerButton.text = owner
                }
            }
            requestCode == REQUEST_PHOTO -> {
                requireActivity().revokeUriPermission(
                    photoUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )
                updatePhotoView()
            }
        }
    }

    private fun getCharacterReport(): String {
        if (character.isPyro) {
            getString(R.string.character_report_pyro)
        } else {
            getString(R.string.character_report_noElement)
        }
        val dateString = DateFormat.format(DATE_FORMAT, character.date).toString()
        val formattedTime = SimpleDateFormat(TIME_FORMAT, Locale.getDefault()).format(character.time)
        val owner = if (character.owner.isBlank()) {
            getString(R.string.character_report_noOwner)
        } else {
            getString(R.string.character_report_owner, character.owner)
        }
        return getString(R.string.character_report, character.title, dateString, formattedTime, owner)
    }

    companion object {
        fun newInstance(characterId: UUID): CharacterFragment {
            val args = Bundle().apply {
                putSerializable(ARG_CHARACTER_ID, characterId)
            }
            return CharacterFragment().apply {
                arguments = args
            }
        }
    }
}

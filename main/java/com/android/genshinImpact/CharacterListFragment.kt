package com.android.genshinImpact

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*
import android.view.LayoutInflater
import android.view.ViewGroup


private const val TAG = "CharacterListFragment"

class CharacterListFragment : Fragment() {

    /**
     * Required for hosting activities
     */
    interface Callbacks {
        fun onCharacterSelected(characterId: UUID)
    }

    private var callbacks: Callbacks? = null

    private lateinit var characterRecyclerView: RecyclerView
    private var adapter: CharacterAdapter? = CharacterAdapter(emptyList())

    private val characterListViewModel: CharacterListViewModel by lazy {
        ViewModelProviders.of(this).get(CharacterListViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_character_list, container, false)

        characterRecyclerView = view.findViewById(R.id.character_recycler_view)
        characterRecyclerView.layoutManager = LinearLayoutManager(context)
        characterRecyclerView.adapter = adapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        characterListViewModel.characterListLiveData.observe(
            viewLifecycleOwner,
            Observer { characters ->
                characters?.let {
                    Log.i(TAG, "Got characters: ${characters.size}")
                    updateUI(characters)
                }
            }
        )
    }

    private fun updateUI(characters: List<Character>) {
        val nonNullCharacters = characters.filterNotNull()
        adapter?.characters = nonNullCharacters
        adapter?.notifyDataSetChanged()
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_character_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_character -> {
                val character = Character()
                characterListViewModel.addCharacter(character)
                callbacks?.onCharacterSelected(character.id)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private inner class CharacterHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        private lateinit var character: Character

        private val titleTextView: TextView = itemView.findViewById(R.id.character_title)
        private val dateTextView: TextView = itemView.findViewById(R.id.character_date)
        private val timeTextView: TextView = itemView.findViewById(R.id.character_time)
        private val pyroImageView: ImageView = itemView.findViewById(R.id.character_pyro)
        private val hydroImageView: ImageView = itemView.findViewById(R.id.character_hydro)
        private val electroImageView: ImageView = itemView.findViewById(R.id.character_electro)
        private val geoImageView: ImageView = itemView.findViewById(R.id.character_geo)
        private val anemoImageView: ImageView = itemView.findViewById(R.id.character_anemo)
        private val cryoImageView: ImageView = itemView.findViewById(R.id.character_cryo)
        private val dendroImageView: ImageView = itemView.findViewById(R.id.character_dendro)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(character: Character?, elementType: ElementType?) {
            // Check if the character is null, if so, hide the views and return
            if (character == null) {
                titleTextView.visibility = View.GONE
                dateTextView.visibility = View.GONE
                timeTextView.visibility = View.GONE
                pyroImageView.visibility = View.GONE
                hydroImageView.visibility = View.GONE
                electroImageView.visibility = View.GONE
                geoImageView.visibility = View.GONE
                anemoImageView.visibility = View.GONE
                cryoImageView.visibility = View.GONE
                dendroImageView.visibility = View.GONE
                return
            }

            // Update UI based on the element type
            titleTextView.text = character.title
            dateTextView.text = formatDateToString(character.date)
            timeTextView.text = formatTimeToString(character.time)

            // Show the views and set the corresponding image for the element type
            titleTextView.visibility = View.VISIBLE
            dateTextView.visibility = View.VISIBLE
            timeTextView.visibility = View.VISIBLE

            pyroImageView.visibility = View.GONE
            hydroImageView.visibility = View.GONE
            electroImageView.visibility = View.GONE
            geoImageView.visibility = View.GONE
            anemoImageView.visibility = View.GONE
            cryoImageView.visibility = View.GONE
            dendroImageView.visibility = View.GONE

            if (elementType != null) {
                when (elementType) {
                    ElementType.PYRO -> pyroImageView.visibility = View.VISIBLE
                    ElementType.HYDRO -> hydroImageView.visibility = View.VISIBLE
                    ElementType.ELECTRO -> electroImageView.visibility = View.VISIBLE
                    ElementType.GEO -> geoImageView.visibility = View.VISIBLE
                    ElementType.ANEMO -> anemoImageView.visibility = View.VISIBLE
                    ElementType.CRYO -> cryoImageView.visibility = View.VISIBLE
                    ElementType.DENDRO -> dendroImageView.visibility = View.VISIBLE
                }
            }

            // Assign the character passed to the property
            this.character = character
        }

        override fun onClick(v: View?) {
            // Check if the character property is initialized before accessing it
            if (::character.isInitialized) {
                callbacks?.onCharacterSelected(character.id)
            } else {
                Log.e(TAG, "Character property is not initialized.")
            }
        }
    }

    private enum class ElementType {
        PYRO, HYDRO, ELECTRO, GEO, ANEMO, CRYO, DENDRO
    }

    private inner class CharacterAdapter(var characters: List<Character>) :
        RecyclerView.Adapter<CharacterHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterHolder {
            val view = layoutInflater.inflate(R.layout.list_item_character, parent, false)
            return CharacterHolder(view)
        }

        override fun getItemCount() = characters.size

        override fun onBindViewHolder(holder: CharacterHolder, position: Int) {
            val character = characters[position]

            val elementType = when {
                character.isPyro -> ElementType.PYRO
                character.isHydro -> ElementType.HYDRO
                character.isElectro -> ElementType.ELECTRO
                character.isGeo -> ElementType.GEO
                character.isAnemo -> ElementType.ANEMO
                character.isCryo -> ElementType.CRYO
                character.isDendro -> ElementType.DENDRO
                else -> null
            }

            holder.bind(character, elementType)
        }
    }

    companion object {
        fun newInstance(): CharacterListFragment {
            return CharacterListFragment()
        }
    }

    private fun formatDateToString(date: Date): String {
        val format = SimpleDateFormat("EEE-yyyy-MM-dd", Locale.getDefault())
        return format.format(date)
    }

    private fun formatTimeToString(time: Date): String {
        val format = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return format.format(time)
    }
}

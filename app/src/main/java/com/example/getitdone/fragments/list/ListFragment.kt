package com.example.getitdone.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.getitdone.R
import com.example.getitdone.data.models.ToDoData
import com.example.getitdone.data.viewmodel.ToDoViewModel
import com.example.getitdone.fragments.SharedViewModel
import com.example.getitdone.fragments.list.adapter.ListAdapter
import com.google.android.material.snackbar.Snackbar
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import kotlinx.android.synthetic.main.fragment_list.view.*


class ListFragment : Fragment() {

    private val mToDoViewModel: ToDoViewModel by viewModels()
    private val mSharedViewModel : SharedViewModel by viewModels()

    private val adapter: ListAdapter by lazy { ListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        //setup RecyclerView
        setUpRecyclerView(view)

        mToDoViewModel.getAllData.observe(viewLifecycleOwner, Observer { data->
            mSharedViewModel.checkIfDatabaseEmpty(data)
            adapter.setData(data)
        })

        mSharedViewModel.emptyDatabase.observe(viewLifecycleOwner, Observer {
            showEmptyDatabaseViews(it)
        })

        view.floatingActionButton.setOnClickListener{
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }

        // set Menu
        setHasOptionsMenu(true)

        return view
    }

    private fun setUpRecyclerView(view: View) {
        val recyclerView = view.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.itemAnimator = SlideInUpAnimator().apply {
            addDuration = 300
        }
        swipeToDelete(recyclerView)
    }

    private fun showEmptyDatabaseViews(emptyDatabase : Boolean) {
        if(emptyDatabase) {
            view?.no_data_imageView?.visibility = View.VISIBLE
            view?.no_data_textView?.visibility = View.VISIBLE
        } else {
            view?.no_data_imageView?.visibility = View.INVISIBLE
            view?.no_data_textView?.visibility = View.INVISIBLE
        }
    }

    private fun swipeToDelete(recyclerView: RecyclerView) {
        val swipeToDeleteCallback = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedItem = adapter.dataList[viewHolder.adapterPosition]
                // Delete Item
                mToDoViewModel.deleteData(deletedItem)
                adapter.notifyItemRemoved(viewHolder.adapterPosition)
                // Restore Deleted Item
                restoreDeletedData(viewHolder.itemView,deletedItem,viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    //adding our option Menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_delete_all) {
            confirmRemoval()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun restoreDeletedData(view: View, deletedItem: ToDoData,position: Int) {
        val snackBar = Snackbar.make(
            view,
            "Deleted '${deletedItem.title}'",
            Snackbar.LENGTH_LONG
        )
        snackBar.setAction("Undo") {
            mToDoViewModel.insertData(deletedItem)
            adapter.notifyItemChanged(position)
        }
        snackBar.show()
    }

    // show AlertDialog to confirm removal of all Items from the database
    private fun confirmRemoval() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") {_,_ ->
            mToDoViewModel.deleteAll()

            Toast.makeText(
                requireContext(),
                "Successfully Removed Everything",
                Toast.LENGTH_SHORT
            ).show()
        }
        builder.setNegativeButton("No") {_,_ -> }

        builder.setTitle("Delete Everything?")
        builder.setMessage("Are you sure you want to remove all Everything?")
        builder.create().show()
    }
}
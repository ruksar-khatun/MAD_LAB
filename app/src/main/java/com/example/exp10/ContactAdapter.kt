package com.example.exp10

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ContactAdapter(
    private var contactsList: List<Contact>,
    private val onContactClick: (Contact) -> Unit,
    private val onShareClick: (Contact) -> Unit
) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvAvatar: TextView = itemView.findViewById(R.id.tv_avatar)
        val tvName: TextView = itemView.findViewById(R.id.tv_name)
        val tvPhone: TextView = itemView.findViewById(R.id.tv_phone)
        val btnShare: ImageButton = itemView.findViewById(R.id.btn_share_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_contact, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contactsList[position]
        holder.tvAvatar.text = contact.initial
        holder.tvName.text = contact.name.ifBlank { "Unknown Name" }
        holder.tvPhone.text = contact.number.ifBlank { "No Phone Number" }

        holder.itemView.setOnClickListener {
            onContactClick(contact)
        }

        holder.btnShare.setOnClickListener {
            onShareClick(contact)
        }
    }

    override fun getItemCount(): Int = contactsList.size

    fun updateData(newContacts: List<Contact>) {
        contactsList = newContacts
        notifyDataSetChanged()
    }
}
package com.example.agata.todolist

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

class ItemCardViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.item_card, parent, false)) {

    private var title : TextView? = null
    private var content : TextView? = null
    private var deadline : TextView? = null
    private var priority : ImageView? = null

    init{
        title = itemView.findViewById(R.id.titleTextView)
        content = itemView.findViewById(R.id.contentTextView)
        deadline = itemView.findViewById(R.id.deadlineTextView)
        priority = itemView.findViewById(R.id.cardImageView)
    }

    fun bind(context: Context, cardItem: CardItem){
        title?.text = cardItem.title
        content?.text = cardItem.content
        deadline?.text = cardItem.deadline
        priority?.setBackgroundColor(ContextCompat.getColor(context, R.color.colorGreen))
    }
}
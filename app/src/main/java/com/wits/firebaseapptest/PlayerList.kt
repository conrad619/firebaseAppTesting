package com.wits.firebaseapptest

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class PlayerList(val context: Activity, val playerList:List<Player>): ArrayAdapter<Player>(context, R.layout.player_list_layout,playerList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        //return super.getView(position, convertView, parent)

        var listView = context.layoutInflater.inflate(R.layout.player_list_layout,null,true)

        var name = listView.findViewById<TextView>(R.id.textViewPlayer)
        var rating = listView.findViewById<TextView>(R.id.TextViewRating)

        var player = playerList.get(position)

        name.text = player.name
        rating.text = player.rating.toString()

        return listView
    }
}
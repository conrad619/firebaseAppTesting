package com.wits.firebaseapptest

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class GameList(val context: Activity, val gameList:List<Game>): ArrayAdapter<Game>(context, R.layout.game_list_layout,gameList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        //return super.getView(position, convertView, parent)

        var listView = context.layoutInflater.inflate(R.layout.game_list_layout,null,true)

        var name = listView.findViewById<TextView>(R.id.textViewName)
        var genre = listView.findViewById<TextView>(R.id.textViewGenre)

        var game = gameList.get(position)

        name.text = game.name
        genre.text = game.genre

        return listView
    }
}
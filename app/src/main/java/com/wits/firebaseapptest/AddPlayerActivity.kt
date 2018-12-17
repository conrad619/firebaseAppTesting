package com.wits.firebaseapptest

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_add_player.*

class AddPlayerActivity : AppCompatActivity() {

    lateinit var databasePlayers:DatabaseReference

    var playerList = arrayListOf<Player>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_player)

        val intent = intent

        val id = intent.getStringExtra(MainActivity.GAME_ID)
        val name = intent.getStringExtra(MainActivity.GAME_NAME)

        textViewGameName.text = name

        databasePlayers = FirebaseDatabase.getInstance().getReference("players").child(id)

        buttonAddPlayer.setOnClickListener(){
            savePlayer()
        }
    }

    override fun onStart() {
        super.onStart()

        databasePlayers.addValueEventListener( object: ValueEventListener {

            override fun onDataChange(data: DataSnapshot) {

                playerList.clear()

                for (playerSnapshot in data.children){
                    val player = playerSnapshot.getValue(Player::class.java)

                    playerList.add(player!!)
                }

                val adapter = PlayerList(this@AddPlayerActivity,playerList)
                listViewPlayer.adapter = adapter
            }

            override fun onCancelled(p0: DatabaseError) {


            }

        })

    }

    fun savePlayer(){
        val playerName = editPlayerName.text.toString().trim()
        val rating = seekBarRating.progress

        if(!TextUtils.isEmpty(playerName)){
            val id = databasePlayers.push().key

            val player = Player(id!!,playerName,rating)

            databasePlayers.child(id).setValue(player)

            Toast.makeText(this,"Player save succesfully!",Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(this,"You should enter player name",Toast.LENGTH_LONG).show()
        }

    }
}

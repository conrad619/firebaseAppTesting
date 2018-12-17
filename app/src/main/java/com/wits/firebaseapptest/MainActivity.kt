package com.wits.firebaseapptest

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.game_update_dialog.*

class MainActivity : AppCompatActivity() {


    lateinit var databaseGame: DatabaseReference
    var gameList = arrayListOf<Game>()


    companion object{
        const val GAME_NAME:String = "gamename";
        const val GAME_ID:String = "gameid";
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        databaseGame = FirebaseDatabase.getInstance().getReference("games")

        buttonAdd.setOnClickListener{
            addGame()
        }


        listViewGames.setOnItemClickListener{_,_,pos,_->
                val game  = gameList[pos]

                intent = Intent(applicationContext,AddPlayerActivity::class.java)

                intent.putExtra(GAME_NAME,game.name)
                intent.putExtra(GAME_ID,game.gameId)

                startActivity(intent)
            }

        listViewGames.setOnItemLongClickListener{_,_,pos,_->

            val game = gameList[pos]

            showUpdateDialog(game.gameId,game.name)

            true
        }






    }

    override fun onStart() {
        super.onStart()

        databaseGame.addValueEventListener( object:ValueEventListener{

            override fun onDataChange(data: DataSnapshot) {

                gameList.clear()

                for (gameSnapshot in data.children){
                    val game = gameSnapshot.getValue(Game::class.java)

                    gameList.add(game!!)
                }

                val adapter = GameList(this@MainActivity,gameList)
                listViewGames.adapter = adapter
            }

            override fun onCancelled(p0: DatabaseError) {


            }

        })

    }

    private fun showUpdateDialog(gameId:String, gameName:String){
        var dialogBuilder = AlertDialog.Builder(this)

        var inflater = layoutInflater

        val dialogView = inflater.inflate(R.layout.game_update_dialog, null)

        dialogBuilder.setView(dialogView)

        dialogBuilder.setTitle("Updating Game"+gameName)

        var alertDialog = dialogBuilder.create()

        alertDialog.show()

        var updateButton = dialogView.findViewById<Button>(R.id.updateGameButton)
        var deleteButton = dialogView.findViewById<Button>(R.id.deleteGameButton)

        updateButton.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                    val editGame = dialogView.findViewById<EditText>(R.id.editGameName)
                    val spinnerUpdate = dialogView.findViewById<Spinner>(R.id.spinnerUpdateGenre)
                    val gameName = editGame.text.toString().trim()
                    val gameGenre = spinnerUpdate.selectedItem.toString()

                    if(!TextUtils.isEmpty(gameName)){

                        updateGame(gameId,gameName,gameGenre)

                        alertDialog.dismiss()
                    }else{
                        Toast.makeText(this@MainActivity,"Set game name",Toast.LENGTH_LONG).show()
                    }
            }

        })

        deleteButton.setOnClickListener(object:View.OnClickListener{
            override fun onClick(v: View?) {

                deleteGame(gameId)

                alertDialog.dismiss()
            }

        })


    }


    private fun updateGame(gameId:String,gameName:String,gameGenre:String):Boolean{
        var databaseReference = databaseGame.child(gameId)

        var game = Game(gameId, gameName,gameGenre)

        databaseReference.setValue(game)

        Toast.makeText(this,"Successfully updated game",Toast.LENGTH_LONG).show()

        return true
    }

    private fun deleteGame(gameId:String){
        val drGame = FirebaseDatabase.getInstance().getReference("games").child(gameId)
        val drPlayers = FirebaseDatabase.getInstance().getReference("players").child(gameId)

        drGame.removeValue()
        drPlayers.removeValue()

        Toast.makeText(this, "Game successfully deleted",Toast.LENGTH_LONG).show()

    }

    fun addGame(){
        var name = editName.text.toString().trim()
        var genre = spinnerGenres.selectedItem.toString()

        if (!TextUtils.isEmpty(name)){

            var id = databaseGame.push().key

            var game = Game(id!!,name,genre)

            databaseGame.child(id!!).setValue(game)

            Toast.makeText(this,"Game added",Toast.LENGTH_LONG).show()

        }else{
            Toast.makeText(this,"You should enter a name",Toast.LENGTH_LONG).show()
        }
    }
}

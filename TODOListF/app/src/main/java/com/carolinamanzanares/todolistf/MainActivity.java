package com.carolinamanzanares.todolistf;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.carolinamanzanares.todolistf.FragmentPackage.InputFragment;
import com.carolinamanzanares.todolistf.Model.ToDo;


public class MainActivity extends ActionBarActivity implements InputFragment.TODOItemListener {

    private final String TODO = "TODO";
    private InputFragment.TODOItemListener listFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try{
            listFragment = (InputFragment.TODOItemListener) getFragmentManager().findFragmentById(R.id.listFragment);
        } catch (ClassCastException ex){
            throw new ClassCastException(this.toString() + " must implement TODOItemListener");
        }
    }


    public void addTodo(ToDo todo){
        listFragment.addTodo(todo);
    }
}



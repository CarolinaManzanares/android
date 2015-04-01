package com.carolinamanzanares.todolistf.FragmentPackage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.carolinamanzanares.todolistf.DetailActivity;
import com.carolinamanzanares.todolistf.Model.ToDo;
import com.carolinamanzanares.todolistf.R;
import com.carolinamanzanares.todolistf.adapters.ToDoAdapter;

import java.util.ArrayList;


/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class TodoListFragment extends ListFragment implements InputFragment.TODOItemListener{

    private ArrayList<ToDo> todos;
    private ArrayAdapter<ToDo> aa;
    private final String TODOS_KEY = "todos_key";
    public static final String TODOS_ITEM = "todos_item";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout =  super.onCreateView(inflater, container, savedInstanceState);

        todos = new ArrayList<>();
        aa = new ToDoAdapter(
                getActivity(), R.layout.todo_list_item, todos);

        if (savedInstanceState != null){

            ArrayList<ToDo> tmp = savedInstanceState.getParcelableArrayList(TODOS_KEY);
            todos.addAll(tmp);
        }

        setListAdapter(aa);

        return layout;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        ToDo todo = todos.get(position);
        Intent detailIntent = new Intent(getActivity(), DetailActivity.class);

        detailIntent.putExtra(TODOS_ITEM, todo);
        startActivity(detailIntent);

    }

    @Override
    public void addTodo(ToDo todo) {
        todos.add(0, todo);
        aa.notifyDataSetChanged();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putParcelableArrayList(TODOS_KEY, todos);
        super.onSaveInstanceState(outState);
    }
}

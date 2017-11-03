package com.projeto.adrielle.cosmeticosfinancas.adapters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.projeto.adrielle.cosmeticosfinancas.model.ItemVenda;
import com.tecnoia.matheus.financascosmeticos.R;

import java.util.List;

/**
 * Created by matheus on 04/09/17.
 */

public class AdapterVendasRealizadas extends ArrayAdapter {
    private FragmentActivity activity;
    private List<ItemVenda> itemVendaList;
    private ListView listViewVendas;
    private BottomSheetDialog dialog;


    public AdapterVendasRealizadas(FragmentActivity activity, List<ItemVenda> itemVendaList, ListView listViewVendas) {
        super(activity, R.layout.adapter_vendas_realizadas);
        this.activity = activity;
        this.itemVendaList = itemVendaList;
        this.listViewVendas = listViewVendas;

    }


    public void atualiza(List<ItemVenda> itemVendaList) {
        this.itemVendaList = itemVendaList;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return itemVendaList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater1 = activity.getLayoutInflater();
        final View view1 = inflater1.inflate(R.layout.adapter_vendas_realizadas, null);

        TextView textViewNome, textViewVendidos, textViewSaldoItens;

        textViewVendidos = view1.findViewById(R.id.text_quantidade_vendidos);
        textViewNome = view1.findViewById(R.id.text_nome_vendas_realizadas);
        textViewSaldoItens = view1.findViewById(R.id.text_saldo_itens);
        final ItemVenda itemVenda = itemVendaList.get(position);


        textViewSaldoItens.setText("R$" + itemVenda.getSaldoItens());


        textViewNome.setText(itemVenda.getNome());
        textViewVendidos.setText("Vendidos: " + itemVenda.getQuantidade());
        listViewVendas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                ItemVenda itemVenda1 = itemVendaList.get(i);
                dialogCancelarVenda(itemVenda1);
                return true;
            }
        });
        return view1;

    }

    private void dialogCancelarVenda(ItemVenda itemVenda1) {
        View modalbottomsheet = activity.getLayoutInflater().inflate(R.layout.modal_bottomsheet_vendas, null);

        dialog = new BottomSheetDialog(activity);
        dialog.setContentView(modalbottomsheet);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        dialog.show();
        LinearLayout linearLayoutAtualizar = modalbottomsheet.findViewById(R.id.linear_atualizar);
        linearLayoutAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //instruções



                dialog.dismiss();


            }
        });
        LinearLayout linearLayoutRemover = modalbottomsheet.findViewById(R.id.linear_remover);
        linearLayoutRemover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //instruções

                dialog.dismiss();


            }
        });


    }
}

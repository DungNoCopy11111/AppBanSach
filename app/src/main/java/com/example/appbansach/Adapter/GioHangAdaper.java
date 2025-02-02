package com.example.appbansach.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbansach.Model.GioHang;
import com.example.appbansach.R;
import com.example.appbansach.Service.APIService;
import com.example.appbansach.Service.DataService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GioHangAdaper extends RecyclerView.Adapter<GioHangAdaper.ViewHoler> {
    Context context;
    int idUser;
    ArrayList<GioHang> gioHangs;
    OnItemClickListener listener;
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
    public GioHangAdaper(Context context, ArrayList<GioHang> gioHangs) {
        this.context = context;
        this.gioHangs = gioHangs;
    }

    public GioHangAdaper(Context context,ArrayList<GioHang> gioHangs, int idUser) {
        this.context = context;
        this.idUser = idUser;
        this.gioHangs = gioHangs;
    }

    @NonNull
    @Override
    public ViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dong_giohang,parent,false);
        return new ViewHoler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoler holder, int position) {
        GioHang gioHang = gioHangs.get(position);
        Picasso.with(context).load(gioHang.getHinhSach()).into(holder.imgSach);
        holder.txtTenSP.setText(gioHang.getTenSach());
        int gb = Integer.parseInt(gioHang.getGiaBan());
        int sl = Integer.parseInt(gioHang.getSoLuong());
        holder.txtGiaSP.setText(gb*sl+"đ");
        holder.txtSoL.setText(gioHang.getSoLuong());
        holder.txtQuasl.setVisibility(View.INVISIBLE);
        holder.imgMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int sl = Integer.parseInt(String.valueOf(holder.txtSoL.getText()));
                if(sl>1){
                    sl--;
                    Log.d("CCC", String.valueOf(sl));
                    holder.txtGiaSP.setText(gb*sl+"đ");
                    DataService db = APIService.getService();
                    Call<String> cb = db.UpdateGioHang(idUser,Integer.parseInt(gioHang.getIdSach()),sl);
                    cb.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                listener.onImgDelClick();
                                notifyDataSetChanged();
                            } else {
                                Toast.makeText(context, "Cập nhật thất bại: " + response.message(), Toast.LENGTH_SHORT).show();
                                //Log.e("UpdateError", "Error updating cart: " + response.errorBody().string());
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                        }
                    });
                }
                holder.txtQuasl.setVisibility(View.INVISIBLE);
                holder.txtSoL.setText(sl+"");
            }
        });
        holder.imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int sl = Integer.parseInt(String.valueOf(holder.txtSoL.getText()));
                int slc = Integer.parseInt(String.valueOf(gioHang.getSlCo()));
                if(sl < slc){
                    sl++;
                    Log.d("CCC", String.valueOf(sl));
                    holder.txtGiaSP.setText(gb*sl+"đ");
                    DataService db = APIService.getService();
                    Call<String> cb = db.UpdateGioHang(idUser,Integer.parseInt(gioHang.getIdSach()),sl);
                    cb.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
//                            Toast.makeText(context,"Thành công",Toast.LENGTH_SHORT).show();
//                            listener.onImgDelClick();
                            if (response.isSuccessful()) {
                                Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_LONG).show();
                                listener.onImgDelClick();
                            } else {
                                Toast.makeText(context, "Cập nhật thất bại: " + response.message(), Toast.LENGTH_SHORT).show();
                                //Log.e("UpdateError", "Error updating cart: " + response.errorBody().string());
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Toast.makeText(context, "Lỗi kết nối mạng", Toast.LENGTH_SHORT).show();
                            Log.e("NetworkError", "Error updating cart: " + t.getMessage());
                        }
                    });
                }
                if(sl==slc){
                    holder.txtQuasl.setVisibility(View.VISIBLE);
                }
                holder.txtSoL.setText(sl+"");
            }
        });
        holder.imgDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataService db = APIService.getService();
                Call<String> cb = db.DeleteGioHang(idUser,Integer.parseInt(gioHang.getIdSach()));
                cb.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(response.body().equals("OK")){
                            Toast.makeText(context,"Thành công",Toast.LENGTH_SHORT).show();
                            gioHangs.remove(gioHang);
                            listener.onImgDelClick();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return gioHangs.size();
    }

    public class ViewHoler extends RecyclerView.ViewHolder{
        ImageView imgSach,imgDel,imgAdd,imgMinus;
        TextView txtTenSP,txtGiaSP,txtSoL,txtQuasl;
        public ViewHoler(@NonNull View itemView) {
            super(itemView);
            imgAdd = itemView.findViewById(R.id.imgAdd);
            imgMinus = itemView.findViewById(R.id.imgMinus);
            imgSach = itemView.findViewById(R.id.imgSp);
            imgDel = itemView.findViewById(R.id.imgDel);
            txtTenSP = itemView.findViewById(R.id.txtTenSP);
            txtGiaSP = itemView.findViewById(R.id.txtGiasp);
            txtSoL = itemView.findViewById(R.id.txtsl);
            txtQuasl = itemView.findViewById(R.id.txtQuasl);
        }
    }
    public interface  OnItemClickListener{
        void onImgDelClick();
    }
}

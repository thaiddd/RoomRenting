package com.example.roomrentalmanagementapp;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// Adapter dùng để liên kết dữ liệu danh sách Room với RecyclerView
public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {

    // Danh sách các phòng cần hiển thị
    private List<Room> roomList;

    // Biến listener để xử lý sự kiện click
    private OnRoomClickListener listener;

    // Interface dùng để lắng nghe sự kiện click trên item và nút xóa
    public interface OnRoomClickListener {
        void onItemClick(Room room, int position);   // Sự kiện khi nhấn vào item
        void onDeleteClick(Room room, int position); // Sự kiện khi nhấn nút xóa
    }

    // Constructor nhận vào danh sách phòng và listener
    public RoomAdapter(List<Room> roomList, OnRoomClickListener listener) {
        this.roomList = roomList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Nạp layout item_room.xml để tạo giao diện cho mỗi dòng trong RecyclerView
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room, parent, false);

        // Trả về đối tượng ViewHolder chứa các view của item
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        // Lấy đối tượng Room tại vị trí hiện tại
        Room room = roomList.get(position);

        // Hiển thị tên phòng
        holder.tvRoomName.setText(room.getName());

        // Hiển thị giá thuê phòng
        holder.tvRoomPrice.setText(String.format("Giá thuê: %.0f VNĐ", room.getPrice()));

        // Kiểm tra trạng thái thuê để hiển thị nội dung và màu sắc phù hợp
        if (room.isRented()) {
            // Nếu phòng đã được thuê
            holder.tvStatus.setText("Tình trạng: Đã thuê (" + room.getTenantName() + ")");
            holder.tvStatus.setTextColor(Color.RED);
        } else {
            // Nếu phòng còn trống
            holder.tvStatus.setText("Tình trạng: Còn trống");
            holder.tvStatus.setTextColor(Color.GREEN);
        }

        // Xử lý sự kiện click vào toàn bộ item
        holder.itemView.setOnClickListener(v -> listener.onItemClick(room, position));

        // Xử lý sự kiện click vào nút xóa
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(room, position));
    }

    @Override
    public int getItemCount() {
        // Trả về số lượng phần tử trong danh sách
        return roomList.size();
    }

    // Lớp ViewHolder dùng để giữ tham chiếu tới các view trong mỗi item
    static class RoomViewHolder extends RecyclerView.ViewHolder {
        TextView tvRoomName, tvRoomPrice, tvStatus;
        ImageButton btnDelete;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);

            // Ánh xạ các thành phần giao diện từ item_room.xml
            tvRoomName = itemView.findViewById(R.id.tvRoomName);
            tvRoomPrice = itemView.findViewById(R.id.tvRoomPrice);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
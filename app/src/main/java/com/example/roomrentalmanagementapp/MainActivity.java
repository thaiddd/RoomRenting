package com.example.roomrentalmanagementapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Room> roomList;
    private RoomAdapter adapter;
    private RecyclerView rvRooms;
    private FloatingActionButton btnAddRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initViews();
    }

    private void initData() {
        roomList = new ArrayList<>();
        // Add some sample data
        roomList.add(new Room("ROOM_1710000000001", "Phòng 101", 1500000, false, "", ""));
        roomList.add(new Room("ROOM_1710000000002", "Phòng 102", 2000000, true, "Nguyễn Văn A", "0987654321"));
    }

    private void initViews() {
        rvRooms = findViewById(R.id.rvRooms);
        btnAddRoom = findViewById(R.id.btnAddRoom);

        adapter = new RoomAdapter(roomList, new RoomAdapter.OnRoomClickListener() {
            @Override
            public void onItemClick(Room room, int position) {
                showRoomDialog(room, position);
            }

            @Override
            public void onDeleteClick(Room room, int position) {
                showDeleteConfirmDialog(position);
            }
        });

        rvRooms.setLayoutManager(new LinearLayoutManager(this));
        rvRooms.setAdapter(adapter);

        btnAddRoom.setOnClickListener(v -> showRoomDialog(null, -1));
    }

    private void showRoomDialog(Room room, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_room, null);
        builder.setView(view);

        EditText etId = view.findViewById(R.id.etRoomId);
        EditText etName = view.findViewById(R.id.etRoomName);
        EditText etPrice = view.findViewById(R.id.etPrice);
        CheckBox cbIsRented = view.findViewById(R.id.cbIsRented);
        EditText etTenantName = view.findViewById(R.id.etTenantName);
        EditText etPhone = view.findViewById(R.id.etPhone);

        // ID luôn không cho phép sửa
        etId.setEnabled(false);

        boolean isEdit = (room != null);
        
        // Thiết lập trạng thái hiển thị ban đầu cho thông tin người thuê
        if (isEdit) {
            builder.setTitle("Sửa thông tin phòng");
            etId.setText(room.getId());
            etName.setText(room.getName());
            etPrice.setText(String.valueOf(room.getPrice()));
            cbIsRented.setChecked(room.isRented());
            etTenantName.setText(room.getTenantName());
            etPhone.setText(room.getPhoneNumber());
            
            // Nếu đã thuê thì hiện, chưa thuê thì ẩn
            etTenantName.setVisibility(room.isRented() ? View.VISIBLE : View.GONE);
            etPhone.setVisibility(room.isRented() ? View.VISIBLE : View.GONE);
        } else {
            builder.setTitle("Thêm phòng mới");
            etId.setHint("ID: Tự động sinh");
            
            // Mặc định thêm mới là chưa thuê nên ẩn
            cbIsRented.setChecked(false);
            etTenantName.setVisibility(View.GONE);
            etPhone.setVisibility(View.GONE);
        }

        // Lắng nghe sự kiện thay đổi checkbox để ẩn/hiện ô nhập liệu
        cbIsRented.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                etTenantName.setVisibility(View.VISIBLE);
                etPhone.setVisibility(View.VISIBLE);
            } else {
                etTenantName.setVisibility(View.GONE);
                etPhone.setVisibility(View.GONE);
                // Xóa trắng dữ liệu khi bỏ tích (tùy chọn)
                etTenantName.setText("");
                etPhone.setText("");
            }
        });

        builder.setPositiveButton("Lưu", (dialog, which) -> {
            String name = etName.getText().toString().trim();
            String priceStr = etPrice.getText().toString().trim();
            boolean isRented = cbIsRented.isChecked();
            String tenantName = isRented ? etTenantName.getText().toString().trim() : "";
            String phone = isRented ? etPhone.getText().toString().trim() : "";

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(priceStr)) {
                Toast.makeText(this, "Vui lòng nhập tên phòng và giá thuê", Toast.LENGTH_SHORT).show();
                return;
            }
            
            if (isRented && TextUtils.isEmpty(tenantName)) {
                Toast.makeText(this, "Vui lòng nhập tên người thuê", Toast.LENGTH_SHORT).show();
                return;
            }

            double price = Double.parseDouble(priceStr);

            if (isEdit) {
                // Cập nhật phòng cũ
                room.setName(name);
                room.setPrice(price);
                room.setRented(isRented);
                room.setTenantName(tenantName);
                room.setPhoneNumber(phone);
                adapter.notifyItemChanged(position);
                Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
            } else {
                // Tự sinh ID mới dựa trên thời gian hệ thống
                String autoId = "ROOM_" + System.currentTimeMillis();
                Room newRoom = new Room(autoId, name, price, isRented, tenantName, phone);
                
                roomList.add(newRoom);
                adapter.notifyItemInserted(roomList.size() - 1);
                rvRooms.scrollToPosition(roomList.size() - 1);
                Toast.makeText(this, "Thêm phòng thành công", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Hủy", null);
        builder.show();
    }

    private void showDeleteConfirmDialog(int position) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa phòng này không?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    roomList.remove(position);
                    adapter.notifyItemRemoved(position);
                    adapter.notifyItemRangeChanged(position, roomList.size());
                    Toast.makeText(this, "Đã xóa phòng", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}

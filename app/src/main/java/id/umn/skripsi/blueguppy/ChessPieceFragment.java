package id.umn.skripsi.blueguppy;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;


public class ChessPieceFragment extends ServiceFragment {

    private static final UUID BATTERY_SERVICE_UUID = UUID.fromString("0000180F-0000-1000-8000-00805f9b34fb");
    private static final UUID BATTERY_LEVEL_UUID = UUID.fromString("00002A19-0000-1000-8000-00805f9b34fb");
    private static Boolean markedBool = false;

    // GATT
    private BluetoothGattService mBatteryService;
    private BluetoothGattCharacteristic mBatteryLevelCharacteristic;

    public ChessPieceFragment(String value, Boolean marked) {
        markedBool = marked;
        updateDescriptor(value);
    }

    TextView textChessPiece1, textChessPiece2, textChessPiece3, textChessPiece4, textChessPiece5, textChessPiece6;
    ImageView imageChessPiece1, imageChessPiece2, imageChessPiece3, imageChessPiece4, imageChessPiece5, imageChessPiece6;

    // Lifecycle callbacks
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chesspiece, container, false);

        textChessPiece1 = (TextView) view.findViewById(R.id.textView_chessPiece1);
        textChessPiece2 = (TextView) view.findViewById(R.id.textView_chessPiece2);
        textChessPiece3 = (TextView) view.findViewById(R.id.textView_chessPiece3);
        textChessPiece4 = (TextView) view.findViewById(R.id.textView_chessPiece4);
        textChessPiece5 = (TextView) view.findViewById(R.id.textView_chessPiece5);
        textChessPiece6 = (TextView) view.findViewById(R.id.textView_chessPiece6);
        imageChessPiece1 = (ImageView) view.findViewById(R.id.imageView_chessPiece1);
        imageChessPiece2 = (ImageView) view.findViewById(R.id.imageView_chessPiece2);
        imageChessPiece3 = (ImageView) view.findViewById(R.id.imageView_chessPiece3);
        imageChessPiece4 = (ImageView) view.findViewById(R.id.imageView_chessPiece4);
        imageChessPiece5 = (ImageView) view.findViewById(R.id.imageView_chessPiece5);
        imageChessPiece6 = (ImageView) view.findViewById(R.id.imageView_chessPiece6);

        setBatteryLevel();
        return view;
    }

    public BluetoothGattService getBluetoothGattService() {
        return mBatteryService;
    }

    @Override
    public ParcelUuid getServiceUUID() {
        return new ParcelUuid(BATTERY_SERVICE_UUID);
    }

    private void setBatteryLevel() {

        int randomPieceNumber = new Random().nextInt(3) + 3; // [0, 3] + 3 => [3, 6]

        if (randomPieceNumber < 6) {
            textChessPiece6.setVisibility(View.INVISIBLE);
            imageChessPiece6.setVisibility(View.INVISIBLE);
        }
        if (randomPieceNumber < 5) {
            textChessPiece5.setVisibility(View.INVISIBLE);
            imageChessPiece5.setVisibility(View.INVISIBLE);
        }
        if (randomPieceNumber < 4) {
            textChessPiece4.setVisibility(View.INVISIBLE);
            imageChessPiece4.setVisibility(View.INVISIBLE);
        }

        ArrayList<ImageView> circleList = new ArrayList<>();
        circleList.add(imageChessPiece1);
        circleList.add(imageChessPiece2);
        circleList.add(imageChessPiece3);
        circleList.add(imageChessPiece4);
        circleList.add(imageChessPiece5);
        circleList.add(imageChessPiece6);

        int pieceInstructions = 0;
        int markedPieceNumber = new Random().nextInt(randomPieceNumber);

        for (int i = 0; i < randomPieceNumber; i++) {
            int randomChessPiece;
            int index = i / 2;

            // ensure first piece is not Queen, ensure last piece is not Queen if markedBool is true
            if (i == 0 || (i == randomPieceNumber-1 && markedBool)) randomChessPiece = new Random().nextInt(3) + 1; // [1, 3]
            else randomChessPiece = new Random().nextInt(4) + 1; // [1, 4]

            if (randomChessPiece == 1)
                circleList.get(i).setImageResource(R.drawable.bishop_black_chess_piece);
            if (randomChessPiece == 2)
                circleList.get(i).setImageResource(R.drawable.knight_black_chess_piece);
            if (randomChessPiece == 3)
                circleList.get(i).setImageResource(R.drawable.rook_black_chess_piece);
            if (randomChessPiece == 4)
                circleList.get(i).setImageResource(R.drawable.queen_black_chess_piece);

            pieceInstructions *= 10;
            pieceInstructions += randomChessPiece;

            if (i == markedPieceNumber && markedBool){
                pieceInstructions *= 10;
                circleList.get(i).setBackgroundResource(R.drawable.marked_piece);
            }

            Log.wtf("Index", String.valueOf(index));
            Log.wtf("Piece Instructions", String.valueOf(pieceInstructions));
        }

        ByteBuffer b = ByteBuffer.allocate(4);
        //b.order(ByteOrder.BIG_ENDIAN); // optional, the initial order of a byte buffer is always BIG_ENDIAN.
        b.putInt(pieceInstructions);

        byte[] result = b.array();
        Log.wtf("Byte", String.valueOf(result[0]));
        Log.wtf("Byte", String.valueOf(result[1]));
        Log.wtf("Byte", String.valueOf(result[2]));
        Log.wtf("Byte", String.valueOf(result[3]));

        mBatteryLevelCharacteristic.setValue(result);
    }

    public void updateDescriptor(String string) {
        mBatteryLevelCharacteristic =
                new BluetoothGattCharacteristic(BATTERY_LEVEL_UUID,
                        BluetoothGattCharacteristic.PROPERTY_READ | BluetoothGattCharacteristic.PROPERTY_NOTIFY,
                        BluetoothGattCharacteristic.PERMISSION_READ);

        mBatteryLevelCharacteristic.addDescriptor(
                Peripheral.getClientCharacteristicConfigurationDescriptor());

        mBatteryLevelCharacteristic.addDescriptor(Peripheral.getCharacteristicUserDescriptionDescriptor(string));

        mBatteryService = new BluetoothGattService(BATTERY_SERVICE_UUID,
                BluetoothGattService.SERVICE_TYPE_PRIMARY);
        mBatteryService.addCharacteristic(mBatteryLevelCharacteristic);
    }
}

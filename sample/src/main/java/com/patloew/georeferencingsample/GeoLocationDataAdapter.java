package com.patloew.georeferencingsample;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.patloew.georeferencingsample.data.Car;
import com.patloew.georeferencingsample.geoData.GeoLocation;
import com.patloew.georeferencingsample.geoData.PointType;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.LongPressAwareTableDataAdapter;

import static java.lang.String.format;


public class GeoLocationDataAdapter extends LongPressAwareTableDataAdapter<GeoLocation> {

    private static final int TEXT_SIZE = 14;
    private static final NumberFormat PRICE_FORMATTER = NumberFormat.getNumberInstance();


    public GeoLocationDataAdapter(final Context context, final List<GeoLocation> data, final TableView<GeoLocation> tableView) {
        super(context, data, tableView);
    }

    @Override
    public View getDefaultCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        final GeoLocation geoLocation = getRowData(rowIndex);
        //final Car car = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 0:
                renderedView = renderPosNumber(geoLocation);
                        //renderProducerLogo(car, parentView);
                break;
            case 1:
                renderedView = renderPositions(geoLocation, parentView);
                break;
            case 2:
                //renderedView = renderPower(car, parentView);
                renderedView = renderOffsets(geoLocation, parentView);
                break;
            case 3:
                renderedView = renderTyp(geoLocation);
                break;
        }

        return renderedView;
    }


    @Override
    public View getLongPressCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        //final Car car = getRowData(rowIndex);
        View renderedView = null;
        renderedView = getDefaultCellView(rowIndex, columnIndex, parentView);
        /*
        switch (columnIndex) {
            case 1:
                renderedView = renderEditableCatName(car);
                break;
            default:
                renderedView = getDefaultCellView(rowIndex, columnIndex, parentView);
        }
*/
        return renderedView;
    }

    private View renderEditableCatName(final Car car) {
        final EditText editText = new EditText(getContext());
        editText.setText(car.getName());
        editText.setPadding(20, 10, 20, 10);
        editText.setTextSize(TEXT_SIZE);
        editText.setSingleLine();
        editText.addTextChangedListener(new CarNameUpdater(car));
        return editText;
    }

    private View renderPrice(final Car car) {
        final String priceString = PRICE_FORMATTER.format(car.getPrice()) + " €";

        final TextView textView = new TextView(getContext());
        textView.setText(priceString);
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(TEXT_SIZE);

        if (car.getPrice() < 50000) {
            textView.setTextColor(ContextCompat.getColor(getContext(), R.color.table_price_low));
        } else if (car.getPrice() > 100000) {
            textView.setTextColor(ContextCompat.getColor(getContext(), R.color.table_price_high));
        }

        return textView;
    }

    private View renderPower(final Car car, final ViewGroup parentView) {
        final View view = getLayoutInflater().inflate(R.layout.table_cell_power, parentView, false);
        final TextView kwView = (TextView) view.findViewById(R.id.kw_view);
        final TextView psView = (TextView) view.findViewById(R.id.ps_view);

        kwView.setText(format(Locale.ENGLISH, "%d %s", car.getKw(), getContext().getString(R.string.kw)));
        psView.setText(format(Locale.ENGLISH, "%d %s", car.getPs(), getContext().getString(R.string.ps)));

        return view;
    }

    private View renderOffsets(final GeoLocation geoLocation, final ViewGroup parentView) {
        final View view = getLayoutInflater().inflate(R.layout.table_cell_position, parentView, false);
        final TextView kwView = (TextView) view.findViewById(R.id.lat_view);
        final TextView psView = (TextView) view.findViewById(R.id.lon_view);

        if(geoLocation.isOffsetPoint()) {
            kwView.setText(geoLocation.getRefLocationNum() + " : " + format(Locale.ENGLISH, "%f %s", geoLocation.getOffsetX(), getContext().getString(R.string.cmX)));
            psView.setText(format(Locale.ENGLISH, "%f %s", geoLocation.getOffsetY(), getContext().getString(R.string.cmY)));
        } else {
            kwView.setText("---");
            psView.setText("---");
        }

        return view;
    }

    private View renderPositions(final GeoLocation geoLocation, final ViewGroup parentView) {
        final View view = getLayoutInflater().inflate(R.layout.table_cell_position, parentView, false);
        final TextView kwView = (TextView) view.findViewById(R.id.lat_view);
        final TextView psView = (TextView) view.findViewById(R.id.lon_view);

        if(geoLocation.getLocation() != null) {
            kwView.setText(format(Locale.ENGLISH, "%f %s", geoLocation.getLocation().getLatitude(), getContext().getString(R.string.latitude)));
            psView.setText(format(Locale.ENGLISH, "%f %s", geoLocation.getLocation().getLongitude(), getContext().getString(R.string.longitude)));
        }

        return view;
    }


    private View renderNic(){return renderString("bobo");}

    private View renderTyp(final GeoLocation geo){
        String wynik = "OSN";
        if(geo.getType() == PointType.LAMPION)
            wynik = "LAM";
        return renderString(wynik);}

    private View renderPosNumber(final GeoLocation geoLocation) { return renderString(String.valueOf(geoLocation.getNum()));}


    private View renderCatName(final Car car) {
        return renderString(car.getName());
    }

    private View renderProducerLogo(final Car car, final ViewGroup parentView) {
        final View view = getLayoutInflater().inflate(R.layout.table_cell_image, parentView, false);
        final ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        imageView.setImageResource(car.getProducer().getLogo());
        return view;
    }

    private View renderString(final String value) {
        final TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(TEXT_SIZE);
        return textView;
    }

    private static class CarNameUpdater implements TextWatcher {

        private Car carToUpdate;

        public CarNameUpdater(Car carToUpdate) {
            this.carToUpdate = carToUpdate;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // no used
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // not used
        }

        @Override
        public void afterTextChanged(Editable s) {
            carToUpdate.setName(s.toString());
        }
    }

}
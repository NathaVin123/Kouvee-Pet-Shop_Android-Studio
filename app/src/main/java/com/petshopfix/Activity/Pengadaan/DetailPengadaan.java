package com.petshopfix.Activity.Pengadaan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.petshopfix.API.ApiClient;
import com.petshopfix.API.Interface.ApiDetailPengadaan;
import com.petshopfix.API.Interface.ApiPengadaan;
import com.petshopfix.API.Interface.ApiProduk;
import com.petshopfix.API.Response;
import com.petshopfix.DAO.DetailPengadaanDAO;
import com.petshopfix.R;
import com.petshopfix.SQLite.DatabaseHandler;
import com.petshopfix.View.Pengadaan.DaftarPengadaan.ShowPengadaan;
import com.petshopfix.View.Pengadaan.DaftarProdukPengadaanTertentu.UpdatePengadaanShow;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class DetailPengadaan extends AppCompatActivity {

    private static final String TAG = "PdfCreatorActivity";
    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;
    private File pdfFile;
    private PdfWriter writer;
    Context context;
    private List<DetailPengadaanDAO> listdtProduk;
    private Button cetakPdf, btnUbah;
    private String nomorPO;
    private TableLayout tablePengadaan;
    private Bundle detailPengadaan;
    private TextView tvNamaSupplier, tvAlamatSupplier, tvNoHpSupplier, tvNoPO, tvTanggalPengadaan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pengadaan);

        setAtribut();
        init();
    }

    private void init() {
        cetakPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(detailPengadaan.getString("status_po").equals("Belum Datang"))
                {
                    updateStok(nomorPO);
                }
                else
                {
                    try {
                        createPdfWrapper();
                    }catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    } catch (DocumentException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        btnUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(detailPengadaan.getString("status_po").equals("Sudah Datang"))
                {
                    Toast.makeText(DetailPengadaan.this, "Pengadaan produk dengan status sudah datang tidak dapat diubah !",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent i = new Intent(getApplicationContext(), UpdatePengadaanShow.class);
                    i.putExtra("nomorPO", nomorPO);
                    i.putExtra("nama_supplier", detailPengadaan.getString("nama_supplier"));
                    startActivity(i);
                }
            }
        });
    }

    private void setAtribut() {
        context = this;
        cetakPdf = (Button) findViewById(R.id.btnCetak);
        btnUbah = (Button) findViewById(R.id.btnUbah);
        detailPengadaan = getIntent().getExtras();
        tvNamaSupplier = (TextView) findViewById(R.id.txtNamaSupplier);
        tvAlamatSupplier = (TextView) findViewById(R.id.txtAlamatSupplier);
        tvNoHpSupplier = (TextView) findViewById(R.id.txtTelpSupplier);
        tvNoPO = (TextView) findViewById(R.id.txtNoPO);
        tvTanggalPengadaan = (TextView) findViewById(R.id.txtTanggalPengadaan);

        tvNamaSupplier.setText(detailPengadaan.getString("nama_supplier"));
        tvAlamatSupplier.setText(detailPengadaan.getString("alamat_supplier"));
        tvNoHpSupplier.setText(detailPengadaan.getString("noTelp_supplier"));
        tvNoPO.setText("No : " + detailPengadaan.getString("nomorPO"));
        tvTanggalPengadaan.setText("Tanggal : " + detailPengadaan.getString("tgl_po"));

        listdtProduk = new ArrayList<DetailPengadaanDAO>();
        nomorPO = detailPengadaan.getString("nomorPO");

        ApiDetailPengadaan apiService = ApiClient.getClient().create(ApiDetailPengadaan.class);
        Call<Response> detailPengadaan = apiService.tampilPengadaan(nomorPO);

        detailPengadaan.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if(!response.body().getDetailPengadaan().isEmpty())
                {
                    listdtProduk.addAll(response.body().getDetailPengadaan());
                    detailPengadaan();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {

            }
        });
    }

    private void detailPengadaan() {
        tablePengadaan = (TableLayout) findViewById(R.id.tablePengadaan);
        TextView C[] = new TextView[4];

        for (int i = 0; i < listdtProduk.size(); i++) {
            TableRow row = new TableRow(this);
            TableRow.LayoutParams lps = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(lps);

            for (int j=0; j<4; j++) {
                C[j] = new TextView(this);
                C[j].setBackgroundColor(Color.WHITE);
                C[j].setTextColor(Color.DKGRAY);
                C[j].setTextSize(12);
                C[j].setBackgroundResource(R.drawable.row_borders);
            }

            C[0].setGravity(Gravity.CENTER_HORIZONTAL);
            C[1].setPaddingRelative(20, 0, 0, 0);
            C[2].setPaddingRelative(50, 0, 0, 0);
            C[3].setGravity(Gravity.RIGHT);
            C[3].setPaddingRelative(0, 0, 100, 0);

            C[0].setWidth(162);
            C[1].setWidth(374);
            C[2].setWidth(269);
            C[3].setWidth(272);

            C[0].setText(String.valueOf(1 + i));
            C[1].setText(listdtProduk.get(i).getNama_produk());
            C[2].setText(String.valueOf(listdtProduk.get(i).getSatuan()));
            C[3].setText(String.valueOf(listdtProduk.get(i).getJumlah_po()));

            row.addView(C[0]);
            row.addView(C[1]);
            row.addView(C[2]);
            row.addView(C[3]);

            tablePengadaan.addView(row, i);
        }
    }

    private void createPdfWrapper() throws FileNotFoundException, DocumentException {
        int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteStoragePermission != PackageManager.PERMISSION_DENIED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)) {
                    showMessageOKCancel("Bolehkah Aplikasi ini menggunakan Penyimpanan ?",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                REQUEST_CODE_ASK_PERMISSIONS);
                                    }
                                }
                            });
                    return;
                }
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSIONS);
            }
            return;
        } else {
            createPdf();
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void addHeader(PdfWriter writer) {
        PdfPTable header = new PdfPTable(2);
        try {
            //set Defaults
            header.setWidths(new int[]{6, 24});
            header.setTotalWidth(527);
            header.setLockedWidth(true);
            header.getDefaultCell().setFixedHeight(40);
            header.getDefaultCell().setBorder(Rectangle.BOTTOM);
            header.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);
            header.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

            //add image
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.logos);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image img = null;
            byte[] byteArray = stream.toByteArray();
            try {
                img = Image.getInstance(byteArray);
            } catch (BadElementException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            img.scaleAbsolute(200,200);
            img.setAlignment(Element.ALIGN_CENTER);
            header.addCell(img);

            //add Text
            PdfPCell text = new PdfPCell();
            text.setPaddingRight(50);
            text.setPaddingBottom(15);
            text.setBorder(Rectangle.BOTTOM);
            text.setBorderColor(BaseColor.LIGHT_GRAY);
            Paragraph p1 = new Paragraph("KOUVEE PET SHOP", new Font(Font.FontFamily.HELVETICA, 16));
            p1.setAlignment(1);
            text.addElement(p1);
            Paragraph p2 = new Paragraph("Jl. Moses Gatotkaca No. 22 Yogyakarta 55281", new Font(Font.FontFamily.HELVETICA, 12));
            p2.setAlignment(1);
            text.addElement(p2);
            Paragraph p3 = new Paragraph("https://simbahlucu.com       Telp. (0274) 357735", new Font(Font.FontFamily.HELVETICA, 10));
            p3.setAlignment(1);
            text.addElement(p3);

            header.addCell(text);

            header.writeSelectedRows(0, -1, 34, 803, writer.getDirectContent());
        } catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }

    private void createPdf() throws FileNotFoundException, DocumentException {
        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/Download");
        if (!docsFolder.exists()) {
            docsFolder.mkdir();
            Log.i(TAG, "Created a new directory for PDF");
        }

        String pdfname = nomorPO+".pdf";
        pdfFile = new File(docsFolder.getAbsolutePath(), pdfname);
        OutputStream output = new FileOutputStream(pdfFile);
        Document document = new Document(PageSize.A4);
        writer = PdfWriter.getInstance(document, output);

        document.open();

        addHeader(writer);
        document.add(new Paragraph("\n\n\n\n\n"));

        Paragraph judul = new Paragraph(" SURAT PEMESANAN \n\n", new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD, BaseColor.BLACK));
        judul.setAlignment(Element.ALIGN_CENTER);
        document.add(judul);

        PdfPTable tables = new PdfPTable(new float[]{16, 8});
        tables.getDefaultCell().setFixedHeight(50);
        tables.setTotalWidth(PageSize.A4.getWidth());
        tables.setWidthPercentage(100);
        tables.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        PdfPCell cellSupplier = new PdfPCell();
        cellSupplier.setPaddingLeft(20);
        cellSupplier.setPaddingBottom(10);
        cellSupplier.setBorder(Rectangle.NO_BORDER);
        Paragraph supplierP = new Paragraph(
                "Kepada Yth : \n" +
                        detailPengadaan.getString("nama_supplier")+"\n" +
                        detailPengadaan.getString("alamat_supplier")+"\n" +
                        detailPengadaan.getString("noTelp_supplier")+"\n",
                new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK)
        );
        cellSupplier.addElement(supplierP);
        tables.addCell(cellSupplier);

        PdfPCell cellPO = new PdfPCell();
        Paragraph PO = new Paragraph(
                "No : " + nomorPO + "\n\n" +
                        "Tanggal : " + detailPengadaan.getString("tgl_po") + "\n",
                new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK)
        );
        PO.setPaddingTop(5);
        tables.addCell(PO);

        document.add(tables);

        Font f = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK);
        document.add(new Paragraph("\n\n\nMohon untuk disediakan produk-produk berikut ini :  \n\n",f));

        PdfPTable table = new PdfPTable(new float[]{1, 5, 2, 2});
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setFixedHeight(30);
        table.setTotalWidth(PageSize.A4.getWidth());
        table.setWidthPercentage(100);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        Font h = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);

        PdfPCell h1 = new PdfPCell(new Phrase("No",h));
        h1.setHorizontalAlignment(Element.ALIGN_CENTER);
        h1.setPaddingBottom(5);
        PdfPCell h2 = new PdfPCell(new Phrase("Nama com.petshopfix.Activity.Penjualan.Produk",h));
        h2.setHorizontalAlignment(Element.ALIGN_CENTER);
        h2.setPaddingBottom(5);
        PdfPCell h3 = new PdfPCell(new Phrase("Satuan",h));
        h3.setHorizontalAlignment(Element.ALIGN_CENTER);
        h3.setPaddingBottom(5);
        PdfPCell h4 = new PdfPCell(new Phrase("Jumlah",h));
        h4.setHorizontalAlignment(Element.ALIGN_CENTER);
        h4.setPaddingBottom(5);

        table.addCell(h1);
        table.addCell(h2);
        table.addCell(h3);
        table.addCell(h4);
        table.setHeaderRows(1);
        PdfPCell[] cells = table.getRow(0).getCells();
        for (int j = 0; j < cells.length; j++) {
            cells[j].setBackgroundColor(BaseColor.GRAY);
        }

        for (int i = 0; i < listdtProduk.size(); i++) {
            String no = String.valueOf(i + 1);
            String nama_produk = listdtProduk.get(i).getNama_produk();
            String satuan = listdtProduk.get(i).getSatuan();
            String jumlah_po = String.valueOf(listdtProduk.get(i).getJumlah_po());

            PdfPCell c1 = new PdfPCell(new Phrase(no, f));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell c2 = new PdfPCell(new Phrase(nama_produk, f));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            c2.setPaddingLeft(20);
            PdfPCell c3 = new PdfPCell(new Phrase(satuan, f));
            c3.setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell c4 = new PdfPCell(new Phrase(jumlah_po, f));
            c4.setHorizontalAlignment(Element.ALIGN_CENTER);

            table.addCell(c1);
            table.addCell(c2);
            table.addCell(c3);
            table.addCell(c4);
        }

        PdfPTable table1 = new PdfPTable(new float[]{5, 5});
        table1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        table1.getDefaultCell().setFixedHeight(30);
        table1.setTotalWidth(PageSize.A4.getWidth());
        table1.setWidthPercentage(100);
        table1.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        document.add(table);

        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
        String tglDicetak = sdf.format(currentTime);

        Paragraph P = new Paragraph("\nDicetak tanggal " + tglDicetak, h);
        P.setAlignment(Element.ALIGN_RIGHT);
        document.add(P);

        document.close();
        previewPdf();
    }

    private void previewPdf() {
        PackageManager packageManager = context.getPackageManager();
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        testIntent.setType("application/pdf");
        List list = packageManager.queryIntentActivities(testIntent, PackageManager.MATCH_DEFAULT_ONLY);

        if (list.size() > 0) {
            Uri uri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            {
                uri = FileProvider.getUriForFile(context, "com.kouvee", pdfFile);
            } else {
                uri = Uri.fromFile(pdfFile);
            }

            Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
            pdfIntent.setDataAndType(uri, "application/pdf");
            pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pdfIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(new Intent(getApplicationContext(), ShowPengadaan.class));
            startActivity(pdfIntent);

        } else {
            Toast.makeText(context, "Download a PDF Viewer to see the generated PDF", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateTransaksiPengadaan(String nomorPO) {
        ApiPengadaan apiService = ApiClient.getClient().create(ApiPengadaan.class);
        Call<com.petshopfix.API.Response> pengadaan = apiService.updateStatusPengadaan(nomorPO, "Sudah Datang");

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        pengadaan.enqueue(new Callback<com.petshopfix.API.Response>() {
            @Override
            public void onResponse(Call<com.petshopfix.API.Response> call, retrofit2.Response<Response> response) {
                if(response.code() == 200)
                {
                    progressDialog.dismiss();
                    System.out.println("Success");
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(context, t.getCause().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateStok(String nomorPO) {
        ApiDetailPengadaan apiService = ApiClient.getClient().create(ApiDetailPengadaan.class);
        Call<com.petshopfix.API.Response> pengadaan = apiService.tampilPengadaan(nomorPO);

        pengadaan.enqueue(new Callback<com.petshopfix.API.Response>() {
            @Override
            public void onResponse(Call<com.petshopfix.API.Response> call, retrofit2.Response<Response> response) {
                if (!response.body().getDetailPengadaan().isEmpty())
                {
                    for (int i=0; i<response.body().getDetailPengadaan().size() ; i++)
                    {
                        String id_produk = response.body().getDetailPengadaan().get(i).getId_produk();
                        int jumlah_po = response.body().getDetailPengadaan().get(i).getJumlah_po();
                        int stok = response.body().getDetailPengadaan().get(i).getStok();
                        updateStokProduk(id_produk, (stok+jumlah_po));
                    }
                    updateTransaksiPengadaan(nomorPO);

                    try {
                        createPdfWrapper();
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    } catch (DocumentException ex) {
                        ex.printStackTrace();
                    }

                    Toast.makeText(context, "Barang yang dipesan sudah tersimpan dalam sistem", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<com.petshopfix.API.Response> call, Throwable t) {
                Toast.makeText(context, "Gagal", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateStokProduk(String id_produk, int stok) {
        DatabaseHandler db = new DatabaseHandler(this);
        String NIP = db.getUser(1).getNIP();
        ApiProduk apiService = ApiClient.getClient().create(ApiProduk.class);
        Call<com.petshopfix.API.Response> produks = apiService.updateStok(id_produk, stok, NIP);

        produks.enqueue(new Callback<com.petshopfix.API.Response>() {
            @Override
            public void onResponse(Call<com.petshopfix.API.Response> call, retrofit2.Response<Response> response) {
                if(!response.body().getDetailPengadaan().isEmpty())
                {
                    System.out.println("Success");
                }
            }

            @Override
            public void onFailure(Call<com.petshopfix.API.Response> call, Throwable t) {
                Toast.makeText(context, "Gagal", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

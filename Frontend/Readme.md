## Konfigurasi Base URL Database (Build Variants)

Aplikasi ini menggunakan Build Variants Android untuk memudahkan peralihan antara konfigurasi base url database lokal dan remote.

### 1. Konfigurasi BASE URL
* **Cara Mengganti:**
  1.  Buka proyek Anda di Android Studio.
  2.  Di bagian kiri bawah Android Studio, cari file **"Build Gradle kts (Module :app)"** kemudian cari code pada bagian **"BuildTypes"**.
  3.  Silahkan ganti Base URL local seperti yang diinstruksikan pada komentar.
  4.  Sync projek jika sudah

### 2. Versi Local Database (Debug Build)
* **Cara Mengganti:**
    1.  Buka proyek Anda di Android Studio.
    2.  Di bagian kiri ujung atas Android Studio disebelah nama projek, cari windows **"Build"** kemudian cari opsi **"Select Build Variants"**.
    3.  Pilih `debug` untuk module `app` pada kolom "Active Build Variant".

  Dengan memilih `debug`, aplikasi akan mengarahkan koneksi databasenya ke endpoint atau konfigurasi yang telah ditentukan untuk lingkungan pengembangan lokal.

### 3. Versi Remote Database (Release Build)

* **Cara Mengganti:**
    1.  Buka proyek Anda di Android Studio.
    2.  Di bagian kiri ujung atas Android Studio disebelah nama projek, cari windows **"Build"** kemudian cari opsi **"Select Build Variants"**.
    3.  Pilih `release` untuk module `app` pada kolom "Active Build Variant".

  Dengan memilih `release`, aplikasi akan mengarahkan koneksi databasenya ke endpoint atau konfigurasi yang telah ditentukan untuk lingkungan remote/produksi.
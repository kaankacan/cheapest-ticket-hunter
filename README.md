# CheapestTicketHunter

- **DateGenerator**: Uçuş aramalarının yapılacağı tarih listesini oluşturur.
- **FlightSearcher**: Selenium kullanarak Panflights web sitesinde uçuş araması yapar.
- **FlightResultCleaner**: Elde edilen uçuş arama sonuçlarını temizler, gereksiz verileri çıkarır ve uçuş verilerini veritabanına kaydetmeye hazır hale getirir.
- **DataManager**: Eski verileri siler, temizlenmiş verileri veritabanına kaydeder, varış noktalarına ait ortalama fiyatları hesaplayıp veritabanına kaydeder, fiyat kıyaslaması yapar ve tabloda güncelleme yapar, tabloyu Excel dosyasına yazar.
- **ProcessCoordinator**: Tüm süreci koordine eden ve sınıflar arasındaki iş akışını yöneten ana sınıf.



## Nasıl Çalışır:
- Belirlenen kalkış noktalarından (örn. IST, SAW, ADB gibi havalimanları) 250 gün boyunca uçuş aramaları yapılır.
- Her gün, her havalimanından o güne ait en uygun uçak biletleri aranır. Örneğin:
  - **IST - ANYWHERE** (Tarih: 3 Ekim 2024) – Bu şekilde aramalar yapılır 3 Ekimde IST-AMS için 10 uçuş var ise 10 uçuşun en uygun olanı bulunur.
  - **SAW - ANYWHERE** (Tarih: 3 Ekim 2024)
  - **ADB - ANYWHERE** (Tarih: 3 Ekim 2024)
  - **IST - ANYWHERE** (Tarih: 4 Ekim 2024)
  - **SAW - ANYWHERE** (Tarih: 4 Ekim 2024)
  - **ADB - ANYWHERE** (Tarih: 4 Ekim 2024)

- Bulunan uçak bileti verileri gereksiz bilgilerden temizlenir ve veritabanındaki **flights** tablosuna kaydedilir.
- 250 gün boyunca yapılan aramalar sonucunda 60.000’den fazla uçak bileti verisi elde edilir.
- Bu uçak biletleri, varış lokasyonlarına göre gruplandırılarak ortalama fiyatlar hesaplanır ve **destinationAvgPrice** tablosuna kaydedilir.
- Her günün en uygun uçak biletleri ile **destinationAvgPrice** tablosundaki varış noktalarının ortalama fiyatları karşılaştırılır ve fark yüzdesi **flights** tablosundaki **"priceDiffPercentage"** alanına kaydedilir.
- Son olarak **flights** tablosundaki veriler Excel'e kaydedilir.





  

[Örnek Excell Tablosu](https://docs.google.com/spreadsheets/d/1fbZUEdkm9m7sI8kDkdqMiUdNHR3ZYPUd/edit?gid=2115726437#gid=2115726437)


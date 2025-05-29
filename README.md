# MyBankApp - Online Bankacılık Sistemi

## Proje Hakkında
MyBankApp, Spring Boot framework'ü kullanılarak geliştirilmiş modern bir online bankacılık uygulamasıdır. Kullanıcıların hesap oluşturabildiği, para transferi yapabildiği ve hesap yönetimi gerçekleştirebildiği kapsamlı bir web uygulamasıdır.

## Özellikler
- Kullanıcı kaydı ve güvenli giriş sistemi
- Hesap oluşturma ve yönetimi
- Para transferi işlemleri
- Hesap dondurma/çözme mekanizması
- Admin paneli ve yetkilendirme sistemi
- İşlem geçmişi takibi
- Responsive tasarım

## Teknoloji Stack
- Java 21
- Spring Boot 3.2.5
- Spring Security
- Spring Data JPA
- MySQL 8
- Thymeleaf
- Bootstrap 5
- Maven

## Gereksinimler
1. JDK 21 veya üzeri
2. Maven 3.9+
3. MySQL 8.0+
4. Git

## Kurulum Adımları

### 1. Veritabanı Kurulumu
```sql
CREATE DATABASE bankapp;
CREATE USER 'bankappuser'@'localhost' IDENTIFIED BY '20sdk07';
GRANT ALL PRIVILEGES ON bankapp.* TO 'bankappuser'@'localhost';
FLUSH PRIVILEGES;
```

### 2. Projeyi Klonlama
```bash
git clone https://github.com/20sdk07/mybankapp.git
cd mybankapp
```

### 3. Yapılandırma
src/main/resources/application-dev.properties dosyasını oluşturun ve veritabanı bilgilerinizi girin:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bankapp
spring.datasource.username=bankappuser
spring.datasource.password=yourpassword
```

### 4. Uygulamayı Başlatma
```bash
mvn spring-boot:run
```
Uygulama http://localhost:8080 adresinde çalışacaktır.

## Varsayılan Kullanıcılar
- Admin Hesabı: admin@admin.com / admin123
- Test Kullanıcısı: user@user.com / user123

## Proje Yapısı
```
src/
├── main/
│   ├── java/
│   │   └── com/myapp/
│   │       ├── config/        # Yapılandırma sınıfları
│   │       ├── controller/    # MVC Controllers
│   │       ├── model/         # Entity sınıfları
│   │       ├── repository/    # JPA Repositories
│   │       ├── service/       # İş mantığı katmanı
│   │       └── util/          # Yardımcı sınıflar
│   └── resources/
│       ├── static/           # CSS, JS dosyaları
│       └── templates/        # Thymeleaf şablonları
```

## API Endpoints
- `GET /accounts` - Kullanıcı hesaplarını listeler
- `POST /accounts` - Yeni hesap oluşturur
- `GET /accounts/{id}` - Hesap detaylarını gösterir
- `POST /accounts/{id}/transfer` - Para transferi yapar
- `POST /accounts/{id}/freeze` - Hesabı dondurur

## Güvenlik
- Spring Security ile güvenli kimlik doğrulama
- CSRF koruması
- Rol tabanlı yetkilendirme (USER, ADMIN)
- Session yönetimi
- Password encoding

## Diyagramlar

### Sınıf Diyagramı
![Class Diagram](diagrams/Class%20diagram.png)

### Kullanım Durumu Diyagramı
![Use Case Diagram](diagrams/Use%20case%20diagram.png)

### Sıralama Diyagramları
![Transfer Money Sequence](diagrams/sequance%20diagram(transferMoney).png)
![Freeze Request Sequence](diagrams/sequence%20diagram(freezeRequest).png)

## Uygulama Özellikleri

### Kullanıcı İşlemleri
- Kullanıcı kaydı ve girişi
- Şifre yenileme
- Profil güncelleme

### Hesap İşlemleri
- Yeni hesap oluşturma (1.000 ₺ hoşgeldin bonusu)
- Hesap listesini görüntüleme
- Hesap detaylarını görüntüleme
- Hesap silme
- Hesap dondurma talebi oluşturma

### Para Transfer İşlemleri
- Hesaplar arası para transferi
- Transfer geçmişi görüntüleme
- Transfer limitleri kontrolü

### Admin Özellikleri
- Tüm kullanıcıları listeleme ve yönetme
- Hesap dondurma taleplerini onaylama/reddetme
- Dondurulmuş hesapları görüntüleme ve çözme
- Kullanıcı hesaplarını yönetme

### Teknik Özellikler
- Session bazlı güvenlik
- Concurrent işlem kontrolü
- Exception handling ve hata sayfaları
- Responsive tasarım (Bootstrap 5)
- Thymeleaf template engine
- MySQL veritabanı
- JPA/Hibernate ORM
- RESTful API endpoints

## Veritabanı Şeması
- Users: Kullanıcı bilgileri
- Account: Hesap bilgileri
- Transaction: İşlem kayıtları
- FreezeRequest: Dondurma talepleri

## Geliştirici Notları
- Proje Spring Boot 3.2.5 ile geliştirilmiştir
- Maven dependency management kullanılmıştır
- Tüm controller'lar için birim testler mevcuttur
- Service katmanı transaction yönetimi içerir
- Bootstrap 5 ile responsive UI tasarlanmıştır

## Kurulum Sorunları ve Çözümleri

### Maven Bağımlılık Sorunları
```bash
mvn clean install -U
```
komutu ile bağımlılıkları güncellemeyi deneyin.

### MySQL Bağlantı Hatası
1. MySQL servisinin çalıştığından emin olun
2. Kullanıcı yetkilerini kontrol edin
3. application-dev.properties dosyasındaki bağlantı bilgilerini doğrulayın

### Port Çakışması
8080 portu kullanımdaysa, application-dev.properties dosyasına ekleyin:
```properties
server.port=8081
```

## İletişim
Sorularınız ve önerileriniz için [issues](https://github.com/20sdk07/mybankapp/issues) sayfasını kullanabilirsiniz.

## Lisans
Bu proje MIT lisansı altında lisanslanmıştır. Detaylar için `LICENSE` dosyasına bakınız.

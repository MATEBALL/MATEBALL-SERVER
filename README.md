# ⚾ MATEBALL  
> **나와 딱! 맞는 야구 직관 메이트와 연결되는 공간**

<br>

## 👩‍💻 Developers

| 👑 양다진 <br> [@YangDaJin0104](https://github.com/YangDaJin0104) | 송혜음 <br> [@hyeeum](https://github.com/hyeeum) |
|:--:|:--:|
| <img height="500" src="https://github.com/user-attachments/assets/cfbcd5b6-70dc-472c-816b-9520eda4db9c" /> | <img height="500" src="https://github.com/user-attachments/assets/e54f5eb3-4c83-42a6-8f20-576ed7606cc0" /> |

<br>

## 🛠️ Tech Stack

<p align="center">
  <img width="800" alt="기술 스택 이미지" src="https://github.com/user-attachments/assets/238bce59-a21a-41bf-87c6-55f4a05d1711" />
</p>

<p align="center">
  <img width="700" alt="기술 스택 이미지" src="https://github.com/user-attachments/assets/81d50d81-4a38-4cdb-a134-9fe56230da69" />
</p>



Mateball 서비스는 AWS EC2 기반으로 구성되며, 모든 서버 구성 요소는 Docker와 Docker Compose로 컨테이너화 되어 있습니다.  
각 컨테이너는 독립적으로 실행되며, API 서버(Spring Boot), Redis, Nginx, Certbot, Python 크롤러 등으로 구성되어 **모듈화와 책임 분리 원칙**을 따릅니다.

<details>
<summary><strong>📌 기술 스택 상세 보기</strong></summary>

### 💻 Language & Framework
<div align="left">
  <img src="https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=openjdk&logoColor=white"/>
  <img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-badge&logo=Spring-Boot&logoColor=white"/>
  <img src="https://img.shields.io/badge/Python-3776AB?style=for-the-badge&logo=Python&logoColor=white"/>
</div>

### 🗄️ Database & Cache
<div align="left">
  <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=MySQL&logoColor=white"/>
  <img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=Redis&logoColor=white"/>
</div>

### ☁️ Infra & DevOps
<div align="left">
  <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=Docker&logoColor=white"/>
  <img src="https://img.shields.io/badge/Docker Compose-000000?style=for-the-badge&logo=Docker&logoColor=white"/>
  <img src="https://img.shields.io/badge/Nginx-009639?style=for-the-badge&logo=NGINX&logoColor=white"/>
  <img src="https://img.shields.io/badge/AWS EC2-FF9900?style=for-the-badge&logo=amazonaws&logoColor=white"/>
  <img src="https://img.shields.io/badge/AWS RDS-527FFF?style=for-the-badge&logo=amazonaws&logoColor=white"/>
  <img src="https://img.shields.io/badge/AWS S3-569A31?style=for-the-badge&logo=amazonaws&logoColor=white"/>
</div>

### 🔒 Security & Authentication
<div align="left">
  <img src="https://img.shields.io/badge/HTTPS-0052CC?style=for-the-badge&logo=Lets-Encrypt&logoColor=white"/>
  <img src="https://img.shields.io/badge/Certbot-3A3A3A?style=for-the-badge&logo=Let's-Encrypt&logoColor=white"/>
  <img src="https://img.shields.io/badge/SSH-000000?style=for-the-badge&logo=OpenSSH&logoColor=white"/>
</div>

### 🤝 Collaboration & Version Control
<div align="left">
  <img src="https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=Git&logoColor=white"/>
  <img src="https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=GitHub&logoColor=white"/>
</div>
</details>

<br>

## 🧱 Database

<p align="center">
  <img width="800" alt="데이터베이스 이미지" src="https://github.com/user-attachments/assets/4b495699-8ca4-4d57-a41a-b767b6959749" />
</p>

- 유저 기반의 매칭 시스템을 중심으로 설계  
- 그룹, 경기 정보, 매칭 조건 등을 별도 도메인으로 분리  
- 데이터 중복 최소화 및 관계 명확화  
- **1NF ~ 3NF** 정규화를 통해 데이터 무결성과 확장성 확보

<br>


## 📋 요구사항 분석

<p align="center">
  <img width="800" src="https://github.com/user-attachments/assets/8d4151fd-6ca3-4105-bad2-95e4662697a7" />
</p>

### 1. 로그인
- **카카오 소셜 로그인**  
- 생년/성별 수집 → 매칭 조건 자동화 및 신뢰성 확보

### 2. 온보딩
- 닉네임: 2~6자, 공백/특수문자/이모지 불가, 한글 또는 영어만 가능  
- 예외 시 사용자에게 명확한 메시지 제공

### 3. 매칭 조건 설정
- 조건 항목:
  - 응원팀, 응원팀 일치 여부  
  - 관람 스타일  
  - 성별 선호  
  - 매칭 유형(1:1 / 그룹)  
  - 그룹장/그룹원 선택 & 날짜 지정

<br>


### 4. 매칭 생성 ⭐

| 매칭 유형 | 생성 수 제한 |
|-----------|--------------|
| 1:1 매칭   | 최대 3개     |
| 그룹 매칭 | 최대 2개     |

- 그룹 상태: 대기 중 / 완료 / 실패  
- 그룹원 상태: 요청 대기 / 승인 대기 / 승인 완료 / 실패

<br>


### 5. 매칭 알고리즘

#### 매칭 가능 조건  
- **유저 간 나이 차이 5세 이하**

| 항목 | 가중치 | 기준 |
|------|--------|------|
| 응원팀 | 40점 | 설정 조건 일치 여부 |
| 성별 | 35점 | 성별 조건 일치 여부 |
| 관람 스타일 | 25점 | 완전/유사 일치 여부 |

<br>


### 6. 매칭 요청 조건

#### 요청 가능
- 다른 사용자가 생성한 매칭에 요청 가능

#### 예외 조건
- 존재하지 않는 매칭 ID  
- 매칭 완료된 그룹  
- 매칭 생성 수 초과  
- 중복 요청 / 중복 날짜 / 지난 날짜  
- 경기 이틀 전까지 매칭 실패 시 제한

<br>


### 7. 수락 & 거절

- 그룹원만 수락/거절 가능  
- 매칭에 포함되지 않은 유저의 요청 처리 → 예외 처리

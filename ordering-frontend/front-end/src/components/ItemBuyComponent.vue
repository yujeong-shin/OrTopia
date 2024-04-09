<template>
  <v-main>
    <div class="colored-block">
      <h2 style="text-align: center; color: black;">상품 정보</h2>
    </div>
    <br><br>
  <v-container class="scrollable-section">
    <table>
      <thead>
        <tr>
          <th style="font-size: 30px; font-weight: bold;">이미지</th>
          <th style="font-size: 30px; font-weight: bold;" class="text-center">상품명</th>
          <th style="font-size: 30px; font-weight: bold;" class="text-center">수량</th>
          <th style="font-size: 30px; font-weight: bold;" class="text-center">가격</th>
        </tr>
      </thead>
      <tbody>
      <tr v-for="item in myItems" :key="item.id">
        <td style="padding: 30px;">
          <img :src="item.imagePath" alt="대체_텍스트" height="200px" width="200px">
        </td>
        <td class="text-center" style="font-size: 25px; padding: 30px;">{{ item.name }}</td>
        <td class="text-center" style="font-size: 25px; padding: 30px;">{{ item.count }}</td>
        <td class="text-center" style="font-size: 25px; padding: 30px;">{{ item.price * item.count }} 원 </td>
      </tr>
    </tbody>
  </table>
  </v-container>
    <br>
    <div class="colored-block">
      <h2 style="text-align: center; color: black;">정보 입력</h2>
    </div>
  <v-container>
  <v-row>
    <!-- 첫 번째 그리드: 구매자 정보 -->
    <v-col cols="12" sm="6">
      <v-card>
        <v-card-title>구매자 정보</v-card-title>
          <v-card-text>
            <v-text-field
            prepend-icon="mdi-account"
            readonly
            :value="defaultName"
            ></v-text-field>
        <v-text-field
        prepend-icon="mdi-phone"
        type="tel"
        readonly
        :value="defaultPhoneNumber"
        ></v-text-field>
        <v-text-field
        prepend-icon="mdi-email"
        type="email"
        readonly
        :value="defaultEmail"
        ></v-text-field>
        </v-card-text>
      </v-card>
    </v-col>
    <!-- 두 번째 그리드: 받는 사람 정보 -->
    <v-col cols="12" sm="6">
      <v-card>
        <v-card-title>받는 사람 정보</v-card-title>
        <v-card-text>
          <v-form>
            <v-text-field
            label="받는 사람"
            v-model="name"
            prepend-icon="mdi-email"
            ></v-text-field>
            <v-text-field
            label="연락처"
            v-model="phoneNumber"
            prepend-icon="mdi-phone"
            ></v-text-field>
            <v-text-field
            label="주소"
            v-model="address"
            prepend-icon="mdi-lock"
            ></v-text-field>
            <v-text-field
            label="주소 상세"
            v-model="addressDetail"
            prepend-icon="mdi-calendar"
            type="number"
            ></v-text-field>
            <v-text-field
            label="배송 요청 사항"
            v-model="request"
            prepend-icon="mdi-gender-male-female"
            ></v-text-field>
            </v-form>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
  <div class="colored-block">
    <h2 style="text-align: center; color: black;">결제 정보</h2>
  </div>
  <v-container>
    <v-row>
    <!-- 첫 번째 그리드 -->
      <v-col cols="12" sm="6">
        <v-card>
          <v-card-title>결제 금액</v-card-title>
          <v-card-text>
          상품 총 금액 : {{totalPrice}} 원
          </v-card-text>
          <v-card-text>
          배송비 : {{deliveryPrice}} 원 (5만원 이상 구매 시 배달료 무료)
          </v-card-text>
          <v-card-text>
          할인 금액 : <span style="color: red;">{{ dicountPrice }}</span> 원
          </v-card-text>
          <!-- 선 긋기 -->
          <hr style="margin: 5px 0;"> 
          <v-card-text style="text-align: right;">
          총 결제 금액  : <span style="font-weight: bold;">{{price}}</span> 원
          </v-card-text>
        </v-card>
      </v-col>
      <v-col cols="12" sm="6">
        <v-card>
          <v-card-title>쿠폰 선택</v-card-title>
          <v-card-text>
          적용된 쿠폰 : 도훈이 결혼 기념 500원 할인 쿠폰 
            <sapn style="margin-left: 10px;">
              <button  style="background-color: black; color: white; border: none; padding: 10px 15px; border-radius: 5px; cursor: pointer;">쿠폰 불러오기</button>
            </sapn>
            </v-card-text>
            <v-card-title>결제 방법 선택</v-card-title>
            <v-card-text>
              <v-row>
                <v-col cols="6">
                <v-img
                  :class="['my-3', { 'logo-hover': hover }]"
                  src="@/assets/kapay.png"
                  contain
                  height="40"
                  style="cursor: pointer;"
                ></v-img>
                </v-col>
                <v-col cols="6">
                <v-img
                  :class="['my-3', { 'logo-hover': hover }]"
                  src="@/assets/tosspay.png"
                  contain
                  height="40"
                  style="cursor: pointer;"
                ></v-img>
                </v-col>
              </v-row>
          </v-card-text>
         </v-card>
      </v-col>
    </v-row>
  </v-container>
</v-main>
</template>
  
  <script>
    // import axios from 'axios';  
  export default {
    data() {
      return {
        defaultName: "John Doe", // 디폴트 이름
        defaultPhoneNumber: "123-456-7890", // 디폴트 전화번호
        defaultEmail: "example@example.com", // 디폴트 이메일
        name: '',
        phoneNumber: '',
        address:'',
        addressDetail:'',
        request:'',
        myItems: JSON.parse(localStorage.getItem('buyItem')),// 로컬 스토리지에 저장되어 있는 선택한 아이템 불러오기
        totalPrice:0, // 상품 금액
        deliveryPrice:3000, // 배달 금액
        dicountPrice:5000, // 할인 금액
        price:0, // 결재할 금액
      };
    },
    created(){
        // async showMyItems(){
        // }
        this.calculateTotalPrice();
    },
    methods: {
      calculateTotalPrice(){
      this.totalPrice = 0; 
      for (let item of this.myItems) {
        console.log(item.price);
        this.totalPrice += item.price * item.count; // totalPrice에 각 아이템의 가격을 더함
      }
      if(this.totalPrice>=50000){ // 5만원 이상 결제 시 배달료 무료
        this.deliveryPrice = 0;
      }
      },
      updatePrice() {
      // 예시로 임의의 값인 500으로 설정
      this.price = this.totalPrice + this.deliveryPrice - this.dicountPrice;
    }
    },
    mounted() {
    // 컴포넌트가 마운트되면 updatePrice 메서드 호출하여 price 값 업데이트
    this.updatePrice();
  }
    
  };
  </script>
  
  <style scoped>
    .scrollable-section {
        height: 300px; /* 원하는 높이로 조정하세요 */
        overflow-y: auto; /* 수직 스크롤 가능하도록 설정 */
        border: 1px solid #ccc; /* 테두리 선 스타일 추가 */
        border-radius: 5px; /* 테두리 선을 둥글게 만듭니다. */
    }
    .colored-block {
        background-color: white; /* 배경색 지정 */
        padding: 20px; /* 내용의 패딩 */
        border-radius: 5px; /* 테두리 선을 둥글게 만듭니다. */
        padding: 20px;
        margin-left: 180px; /* 좌측 마진 추가 */
        margin-right: 180px; /* 우측 마진 추가 */
        /* margin: 40px; */
    }
    .buyerInfo{
        margin-left: 500px; /* 좌측 마진 추가 */
        margin-right: 500px; /* 우측 마진 추가 */
    }
    table {
      border-collapse: collapse;
      width: 100%;
    }

    th, td {
      border: 1px solid #dddddd;
      text-align: left;
      padding: 8px;
    }

    tr:nth-child(even) {
      background-color: #f2f2f2;
    }
  </style>
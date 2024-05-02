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
        <td class="text-center" >
          <div style="font-size: 25px; padding: 30px;">{{ item.name }}</div>
            <template v-for="(option, index) in item.options" :key="index">
              <div style="font-size: 15px;">{{ option.name }}: {{ option.value }}</div>
           </template>
        </td>
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
            <v-row align="center">
              <v-col cols="9">
                <v-text-field
                  label="우편번호"
                  v-model="zipcode"
                  prepend-icon="mdi-lock"
                  style="max-width: 400px;"
                  readonly 
                ></v-text-field>
              </v-col>
              <v-col cols="3">
                <v-btn @click="openModal">주소 찾기</v-btn>
              </v-col>
            </v-row>
            <v-text-field
            label="주소"
            v-model="address"
            prepend-icon="mdi-lock"
            readonly
            ></v-text-field>
            <v-text-field
            label="주소 상세"
            v-model="addressDetail"
            prepend-icon="mdi-calendar"
            readonly
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
                  @click="kakaoPay"
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

<AddressModal
  v-model="showAddressModal"
  @update:dialog="updateDialog('showAddressModal', $event)"
  @address-selected="handleAddressSelected"
  @close-modal="closeAddressModal"
></AddressModal>
</template>

  <script>
  import axios from 'axios';
  import AddressModal from "@/components/AddressModalToBuy.vue";
  export default {
    components: {
    AddressModal,
  },
    data() {
      return {
        defaultName: "John Doe", // 디폴트 이름
        defaultPhoneNumber: "123-456-7890", // 디폴트 전화번호
        defaultEmail: localStorage.getItem('email'), // 디폴트 이메일
        name: '',
        phoneNumber: '',
        addressId:'',
        address:'',
        addressDetail:'',
        request:'',
        zipcode:' ',
        myItems: JSON.parse(localStorage.getItem('buyItem')),// 로컬 스토리지에 저장되어 있는 선택한 아이템 불러오기
        totalPrice:0, // 상품 금액
        deliveryPrice:3000, // 배달 금액
        dicountPrice:5000, // 할인 금액
        price:0, // 결재할 금액
        showAddressModal:false,
      };
    },
    created(){
        // async showMyItems(){
        // }
        this.myInfo();
        this.calculateTotalPrice();
    },
    methods: {
      handleAddressSelected(address) {
      // 선택한 주소 정보를 처리하는 로직을 구현합니다.
        this.address = address.roadAddress;
        this.addressDetail = address.detail;
        this.zipcode = address.zonecode;
        this.addressId = address.id;
        console.log("Selected Address:", address);
      // 이제 여기서 선택한 주소 정보를 부모 컴포넌트의 데이터에 저장하거나 다른 작업을 수행할 수 있습니다.
      },
      updateDialog(key, value) {
      this[key] = value; // key에 해당하는 데이터를 업데이트
      },
      closeAddressModal() {
        this.showAddressModal = false; // 모달 창 닫기
      },    
      openModal() {
        this.showAddressModal = true; // 모달을 열기 위한 데이터 프로퍼티를 토글합니다.
      },
      async myInfo(){
      try{
        const token = localStorage.getItem('accessToken');
        const refreshToken = localStorage.getItem('refreshToken');
        const headers = token ? { Authorization: `Bearer ${token}`, 'X-Refresh-Token': `${refreshToken}` } : {};
        const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/ortopia-member-service/member/${localStorage.getItem('email')}`, { headers });
        console.log(response);
        this.defaultName = response.data.result.name;
        this.defaultPhoneNumber = response.data.result.phoneNumber;
      }catch(e){
        alert(e.message);
      }
      },
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
      },
      async kakaoPay() {
        const token = localStorage.getItem('accessToken');
        const refreshToken = localStorage.getItem('refreshToken');
        try {
          const itemList = this.myItems.map(item => ({ id: item.id, count: item.count, name : item.name, options:item.options }));
          const body = { price: this.price, itemDtoList: itemList }; // itemList을 itemDtoList로 변경
          const headers = token ? { Authorization: `Bearer ${token}`, 'X-Refresh-Token': `${refreshToken}` } : {};
          const data = await axios.post(`${process.env.VUE_APP_API_BASE_URL}/ortopia-order-service/payment/ready`, body, { headers });
          console.log(data);
          const reDirectURL = data.data.result.next_redirect_pc_url;
          // window.open(reDirectURL, '_blank');// 결제 창을 하나 띄우기
          const order = {
            name: this.name,
            totalPrice : this.price,
            phoneNumber: this.phoneNumber,
            addressId: this.addressId,
            request: this.request
          }
          localStorage.setItem("order",JSON.stringify(order));
          window.location.href = reDirectURL; // 페이지 이동
        } catch (e) {
          console.log(e);
          alert(e.response.data.message);
        }
      },
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
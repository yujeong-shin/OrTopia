<template>
  <v-dialog :value="dialog" @input="updateDialog" max-width="800px">
    <v-card>
      <v-card-title class="headline grey lighten-2 text-center">
        배송지 목록
      </v-card-title>
      <v-card-text>
        <v-container>
          <v-row>
            <v-col cols="12">
              <v-card class="mb-2 elevation-2" v-for="address in addresses" :key="address.id">
                <v-card-title>{{ address.name }}</v-card-title>
                <v-card-text>
                  <div>우편번호: {{ address.zonecode }}</div>
                  <div>{{ address.sido }} {{ address.sigungu }} {{ address.bname }} {{ address.roadAddress }} {{ address.detail }}</div>
                </v-card-text>
                <v-card-actions>
                  <v-spacer></v-spacer>
                  <v-btn color="error" icon @click="deleteAddress(address.id)">
                    <v-icon>mdi-delete</v-icon>
                  </v-btn>
                </v-card-actions>
              </v-card>
            </v-col>
          </v-row>
        </v-container>
        <v-divider class="my-5"></v-divider>
        <v-row>
          <v-col cols="12" md="4">
            <v-text-field
              v-model="name"
              label="배송지명"
              placeholder="배송지명을 입력해주세요"
              outlined
              dense
            ></v-text-field>
          </v-col>
          <v-col cols="12" md="4">
            <v-text-field
              v-model="roadAddress"
              readonly
              label="주소"
              append-icon="mdi-map-search"
              @click:append="openPostcode"
              placeholder="주소를 검색해 주세요"
              outlined
              dense
            ></v-text-field>
          </v-col>
          <v-col cols="12" md="4">
            <v-text-field
              v-model="detail"
              label="세부 주소"
              placeholder="세부 주소를 입력해주세요"
              outlined
              dense
            ></v-text-field>
          </v-col>
        </v-row>
        <v-row>
          <v-col class="text-right">
            <v-btn color="success" class="mt-3" @click="addAddress">
              추가
            </v-btn>
          </v-col>
        </v-row>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="primary" @click="close">
          닫기
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>
  
<script>
import axios from 'axios';

export default {
  props: {
    dialog: Boolean,
  },
  emits: ['update:dialog'],
  data() {
    return {
      addresses: [],
      id: '',
      name: '', 
      roadAddress: '',
      zonecode: '',
      sido: '',
      sigungu: '',
      bname: '',
      detail: '',
      
    };
  },
  methods: {
    async deleteAddress(addressId) {
  if (!confirm("이 주소를 삭제하시겠습니까?")) {
      return;
  }

  try {
      const email = localStorage.getItem('email');
      await axios.delete(`${process.env.VUE_APP_API_BASE_URL}/member-service/address/delete/${addressId}`, {
          headers: {
              Authorization: `Bearer ${localStorage.getItem('accessToken')}`,
              "X-Refresh-Token": localStorage.getItem('refreshToken'),
              "myEmail": email, // 헤더에 이메일 정보 추가
          }
      });
      this.getMemberInfo(); // 주소 목록 새로고침
  } catch (error) {
      console.error("주소 삭제 중 오류 발생: ", error);
      alert("주소를 삭제하는 동안 오류가 발생했습니다.");
  }
},
    updateDialog(value) {
      this.$emit('update:dialog', value);
    },
    close() {
      this.$emit('update:dialog', false);
    },
    async getMemberInfo() {
  try {
    const email = localStorage.getItem('email');
    const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/member-service/address/by-email`, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem('accessToken')}`,
        "X-Refresh-Token": localStorage.getItem('refreshToken'),
        "myEmail": email,
      }
    });
    console.log(response); // 응답 구조 확인
    this.addresses = response.data.result; // 가정된 응답 구조에 따라 조정
  } catch (error) {
    console.error("Error fetching member addresses: ", error);
  }
},
    openPostcode() {
      new window.daum.Postcode({
      oncomplete: (data) => {
        this.zonecode = data.zonecode;
        this.roadAddress = data.roadAddress;
        this.sido = data.sido;
        this.sigungu = data.sigungu;
        this.bname = data.bname;
      },
    }).open();
    },
    async addAddress() {
  try {
    const addressData = {
      name: this.name, // 사용자 입력 주소명
      roadAddress: this.roadAddress,
      zonecode: this.zonecode,
      sido: this.sido,
      sigungu: this.sigungu,
      bname: this.bname,
      detail: this.detail,
    };
    // URL에 이메일을 직접 포함하여 요청 보냄
    const email = localStorage.getItem('email');
    await axios.post(`${process.env.VUE_APP_API_BASE_URL}/member-service/address/create`, addressData, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem('accessToken')}`,
        "X-Refresh-Token": localStorage.getItem('refreshToken'),
        myEmail : `${email}`,
      }
    });
    this.getMemberInfo(); // 주소 목록 새로고침
    this.close(); // 대화상자 닫기
  } catch (error) {
    console.error("Error adding new address: ", error);
  }
},
  },
  mounted() {
    this.getMemberInfo();
  },
};
</script>
  
  <style scoped>
  .address-entry {
    margin-bottom: 50px;
  }
  </style>
<template>
    <v-dialog :value="dialog" @input="updateDialog" max-width="1400px">
      <v-col cols="12" md="12" class="text-right">
            <v-btn
              color="gray"
              class="mr-2"
              @click="openNestedModal()"
              >판매물품 등록</v-btn
            >
            <ManageItemsModal
              v-model="nestedModalOpen"
              :dialog="nestedModalOpen" :detail="selectedDetail"  @update:dialog="nestedModalOpen = $event"/>
            <v-btn color="gray" class="mr-2" @click="goToCouponPage"
              >쿠폰 등록</v-btn
            >
          </v-col>
      <v-card>
        <v-card-title class="headline grey lighten-2 text-center">
          판매물품관리
        </v-card-title>
        <v-data-table 
        v-model:expanded="expanded"
        :headers="headers" 
        :items="myItems"
        show-expand
        item-value="name"
        show-select
        >
        <template v-slot:expanded-row="{ item }">
          <tr>
          </tr>
          <tr>
            <th></th>
            <th></th>
            <th v-for="(option, index) in item.optionName" :key="index">{{ option }}</th>
            <th>재고</th>
            <th>수량 조정 </th>
            <th>변경사항 저장</th>
          </tr>
          <tr v-for="(itemOption,index) in item.itemOptionQuantityResponseDtos" :key="itemOption.id">
            <td></td>
            <td>옵션 {{ index + 1 }}</td>
            <td>{{ itemOption.value1 === 'NONE' ? '-' : itemOption.value1 }}</td>
            <td>{{ itemOption.value2 === 'NONE' ? '-' : itemOption.value2 }}</td>
            <td>{{ itemOption.value3 === 'NONE' ? '-' : itemOption.value3 }}</td>
            <td>{{ itemOption.quantity }}</td>
            <td>
              <input type="number" v-model="itemOption.quantity" style="width: 80px;" />
            </td>
            <td >
              <button class="custom-button" @click="editItem(itemOption)">수정</button>
            </td>
          </tr>
        </template>
        </v-data-table>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="primary" @click="close">닫기</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </template>
  
  <script>
  import axios from 'axios';
  import ManageItemsModal from "@/components/ManageItemsModal.vue";
  export default {

    components: {
      ManageItemsModal,
    },
    created() {
        this.getMyItem();
    },
    props: {
      dialog: Boolean,
    },
    emits: ["update:dialog"],
    data() {
      return {
        nestedModalOpen: false,
        expanded: [
        ],
        selected: [],
        myItems : [],
        showModal: false,
        headers: [
        { title: '이름', key: 'name' },
        { title: '가격', key: 'price' },
        { title: '평점', key: 'totalScore' },
        { title: '리뷰 수', key: 'reviewNumber' },
        { title: '최소수량', key: 'minimumStock' }, // 새로운 헤더 추가
        { title: '옵션 보기', key: 'data-table-expand' },
        { title: '', key: '' },
      ],
      };
    },
    methods: {
      openNestedModal(detail) {
      this.selectedDetail = detail;
      console.log(this.selectedDetail);
      this.nestedModalOpen = true; // 하위 모달 창 열기
    },
      async editItem(itemOption) {
        console.log("수정하기 버튼 클릭:", itemOption);
        const token = localStorage.getItem("accessToken");
        const refreshToken = localStorage.getItem("refreshToken");
        try{
          const response  = await axios.post(
              `${process.env.VUE_APP_API_BASE_URL}/ortopia-item-service/item/update/quantity`,itemOption,
                  {
                  headers: {
                      Authorization: `Bearer ${token}`,
                      "X-Refresh-Token": `${refreshToken}`,
                      },
                  }
              );
          console.log(response);
          alert("수정 완료!");
          location.reload();
        }catch(e){
          console.log(e);
        }
        // 아이템을 수정하는 로직을 추가할 수 있습니다.
      },
      // 내가 등록한 아이템을 가져오기.
      async getMyItem(){
          const token = localStorage.getItem("accessToken");
          const refreshToken = localStorage.getItem("refreshToken");
          try{
              const response  = await axios.get(
              `${process.env.VUE_APP_API_BASE_URL}/ortopia-item-service/item/myItem`,
                  {
                  headers: {
                      Authorization: `Bearer ${token}`,
                      "X-Refresh-Token": `${refreshToken}`,
                      },
                  }
              );
              console.log(response);
              this.myItems = response.data.result;
              this.myItems.forEach((item) => {
              // 각 아이템의 score와 reviewNumber를 이용하여 totalScore 계산
              item.totalScore = item.reviewNumber !== 0 ? (item.score / item.reviewNumber).toFixed(2) : 0;
              while (item.optionName.length < 3) {
                item.optionName.push('-'); // 부족한 부분에는 "-"를 추가합니다.
              }
          });
          }catch(e){
              console.log(e);
          }
      },
      updateDialog(value) {
          this.$emit("update:dialog", value);
      },
      close() {
          this.$emit("update:dialog", false);
      },
  },
};
  </script>
  <style>
  .custom-button {
  background-color: black;
  color: white;
  border: none;
  padding: 8px 16px;
  cursor: pointer;
  border-radius: 4px;
}

.custom-button:hover {
  background-color: darkgray;
}
  </style>
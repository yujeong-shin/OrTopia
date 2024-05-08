<template>
    <v-container class="mt-5">
      <v-row justify="center">
        <v-col cols="15" sm="8">
          <v-card>
            <v-card-title class="text-center">
              <h1>장바구니 리스트</h1>
            </v-card-title>
            <v-card-text>
              <v-row justify="space-between" class="mb-3">
                <v-btn color="secondary" @click="clearCart">장바구니 비우기</v-btn>
                <v-btn color="success" @click="placeOrder">주문하기</v-btn>
              </v-row>
              <v-table>
                <thead>
                    <tr>
                    <th class="text-center"></th> 
                    <th class="text-center">상품 사진</th>
                    <th class="text-center">상품 이름</th>
                    <th class="text-center">상품 수량</th>
                    <th class="text-center">금액 </th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="(item, index) in getCartItems" :key="item.itemId">
                    <td class="text-center"> 
                        <v-checkbox
                        class="text-center"
                        v-model="selectedItems[index]"
                        ></v-checkbox>
                    </td>
                    <td><v-img :src="item.imagePath" height="200px"></v-img></td>
                    <td class="text-center" >
                    <div style="font-size: 25px; padding: 30px;">{{ item.name }}</div>
                      <template v-for="(option, index) in item.options" :key="index">
                        <div style="font-size: 15px;">{{ option.name }}: {{ option.value }}</div>
                      </template>
                    </td>
                    <td class="text-center">{{ item.count }}</td>
                    <td class="text-center">{{ item.price * item.count }} 원 </td>
                    </tr>
                </tbody>
            </v-table>
            </v-card-text>
          </v-card>
        </v-col>
      </v-row>
    </v-container>
  </template>

<script>
import {mapGetters} from 'vuex'
import { mapActions } from 'vuex';
export default {
    data(){
        return{
            selectedItems :{},
        }
    },
    computed:{
        ...mapGetters(['getCartItems','getTotalQuantity'])
    },
    methods:{
        ...mapActions([`clearCart`]),
        clearCart(){
            this.$store.dispatch('clearCart')
        },
        async placeOrder(){
          const token = localStorage.getItem("accessToken");
          if(token == null){
              alert("로그인 후 사용해 주세요.")
              this.$router.push(`/login`);
              return;
            }
            console.log(this.selectedItems);
            this.itemList = [...this.getCartItems];
            console.log(this.itemList);
            const orderItems = Object.keys(this.selectedItems)
                                .filter(key=>this.selectedItems[key]==true)
                                .map(key=> {
                                    const item =  this.itemList[key];
                                    return item;
                                });
            if(orderItems.length<1){
                alert("주문할 상품을 선택해 주세요.");
                return;
            }
            if(confirm(`${orderItems.length}개의 종류의 아이템을 주문하시겠습니까?`)){
              localStorage.setItem('buyItem', JSON.stringify(orderItems)); // orderItems를 Vuex 상태에 저장
                this.$router.push('/buypage');
            }else{
                console.log("주문이 취소되었습니다.")
                return; 
            }
            
        },
    }
}
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
  </style>
<template>
    <v-dialog :value="dialog" @input="updateDialog" max-width="1000px"  style="z-index: 9999; margin: 50px;">
      <v-card class="custom-card">
        <v-card-title class="text-center">리뷰 작성</v-card-title>
        <div>
          점수 : <v-icon
            v-for="(icon, index) in maxRating"
            :key="index"
            :color="index < userRating ? activeColor : inactiveColor"
            @click="updateRating(index + 1)"
            @mouseover="updateHoverRating(index + 1)"
            @mouseleave="resetRating()"
          >
            {{ getIcon(index) }}
          </v-icon>
        </div>
        <br>
        <!-- 상품 사진을 업로드할 수 있는 input 태그 -->
      <input type="file" @change="handleFileUpload" accept="image/*">
      
      <!-- 업로드한 이미지를 미리 보여주는 엘리먼트 -->
        <v-row>
         <!-- 이미지 미리보기 -->
        <v-col cols="6" class="image-preview-column"> 
          <div v-if="imagePreview" style="margin-top: 10px;">
          <img :src="imagePreview" alt="Uploaded Image"  style="width: 400px; height: 200px;">
          </div>
        </v-col>
        <!-- 리뷰 내용 입력 -->
        <v-col cols="6">
        <v-textarea
          v-model="reviewContent"
          label="리뷰 내용"
          placeholder="리뷰를 작성해주세요."
          outlined
          rows="8" 
        ></v-textarea>
        </v-col>
    </v-row>
    <v-btn color="primary" @click="createReview(detail)">리뷰 작성</v-btn>
  </v-card>
  </v-dialog>
  </template>
  
  <script>
  import axios from 'axios';
  export default {
    props: {
      dialog: Boolean,
      detail: Object,
    },
    data() {
      return {
        userRating: 0,
        hoverRating: 0,
        maxRating: 5,
        activeColor: 'yellow', // 활성화된 별표 색상
        inactiveColor: 'grey', // 비활성화된 별표 색상
        reviewContent: '', // 리뷰 내용을 저장하는 데이터
        file: null,
        imagePreview: 'https://www.foodsafetykorea.go.kr/residue/assets/_new/img/noimage.gif', // 업로드한 이미지를 미리 보여주기 위한 데이터
      };
    },
    methods: {
      async createReview(detail){
        console.log(detail);
        console.log(this.userRating);
        const email = localStorage.getItem("email");
        try{
        const formData = new FormData();
            // Add item data
        formData.append('score', this.userRating);
        formData.append('content', this.reviewContent);
        formData.append('itemId', detail.itemId);
        formData.append('buyerEmail', email);
        formData.append('orderDetailId', detail.id);
        formData.append('imagePath',this.file);
        const response = await axios.post(
          `${process.env.VUE_APP_API_BASE_URL}/item-service/review/create`, 
          formData, 
          { 
            headers: { 
              'Content-Type': 'multipart/form-data',
              'Authorization': `Bearer ${localStorage.getItem('accessToken')}`, 
              'X-Refresh-Token': `${localStorage.getItem('refreshToken')}`,
            }
          }
        );
        console.log(response);
        alert("리뷰 작성 성공!");
        this.$router.push(`/item/${detail.itemId}`);
        }catch(e){
          console.log(e);
        }
      },
      updateDialog(value) {
        this.$emit("update:dialog", value);
      },
      updateRating(rating) {
        this.userRating = rating;
        // 여기서 서버에 별점을 전송하거나 다른 작업을 수행할 수 있습니다.
      },
      updateHoverRating(rating) {
        this.hoverRating = rating;
      },
      resetRating() {
        this.hoverRating = 0;
      },
      getIcon(index) {
        if (this.hoverRating >= index + 1) {
          return 'mdi-star'; // 사용자가 마우스를 올린 별 아이콘
        } else if (this.userRating >= index + 1) {
          return 'mdi-star'; // 사용자가 클릭한 별 아이콘
        } else {
          return 'mdi-star-outline'; // 비활성화된 별 아이콘
        }
      },
      handleFileUpload(event) {
      this.file = event.target.files[0];
      if (this.file) {
        // 파일을 읽어서 미리보기에 표시
        const reader = new FileReader();
        reader.onload = () => {
          this.imagePreview = reader.result;
        };
        reader.readAsDataURL(this.file);
      }
      },
    },
  };
  </script>
  
<style>
  .v-icon {
    font-size: 24px;
  }
  .image-preview-column {
  display: flex;
  justify-content: flex-end;
  padding-right: 15px;
}

.image-preview-wrapper {
  margin-top: 10px;
}

.image-preview {
  max-width: 100%;
  height: auto;
}
.custom-card {
  margin-left: 50px;
  margin-right: 50px;
}
</style>
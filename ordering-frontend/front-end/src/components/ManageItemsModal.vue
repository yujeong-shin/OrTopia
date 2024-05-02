<template>
  <v-dialog :value="dialog" @input="updateDialog" max-width="1000px">
    <v-card>
      <v-card-title class="headline grey lighten-2 text-center">
        판매물품관리
      </v-card-title>
      <v-container>
        <v-form
          ref="form"
          @submit.prevent="submitItem"
          enctype="multipart/form-data"
        >
          <v-text-field
            v-model="item.name"
            label="아이템 이름"
            required
          ></v-text-field>
          <v-text-field
            v-model="item.stock"
            label="재고"
            type="number"
            required
          ></v-text-field>
          <v-text-field
            v-model="item.price"
            label="가격"
            type="number"
            required
          ></v-text-field>
          <v-select
            v-model="item.category"
            :items="categories"
            label="카테고리"
            required
          ></v-select>
          <v-container v-for="(option, index) in optionList" :key="index">
            <v-row>
              <v-col cols="12" md="6">
                <v-text-field
                  v-model="option.optionName"
                  label="옵션 이름"
                  required
                ></v-text-field>
              </v-col>
              <v-col cols="12" md="6">
                <v-btn @click="addOptionDetail(index)" color="primary"
                  >옵션 내용 추가하기</v-btn
                >
                <v-btn @click="removeOption(index)" color="error"
                  >옵션 삭제하기</v-btn
                >
              </v-col>
            </v-row>
            <v-row
              v-for="(detail, detailIndex) in option.details"
              :key="detailIndex"
            >
              <v-col cols="12" md="6">
                <v-text-field
                  v-model="option.details[detailIndex]"
                  label="옵션 내용"
                  required
                ></v-text-field>
              </v-col>
              <v-col cols="12" md="6">
                <v-btn
                  @click="removeOptionDetail(index, detailIndex)"
                  color="error"
                  >옵션 내용 삭제하기</v-btn
                >
              </v-col>
            </v-row>
          </v-container>
          <v-btn @click="addOption" color="primary">옵션 추가하기</v-btn>
          <v-text-field
            readonly
            :value="item.detail"
            @click="showModal = true"
            label="상세 설명"
            required
            append-icon="mdi-pencil"
          ></v-text-field>
          <!-- HTML 콘텐츠를 렌더링하는 부분 -->
          <div v-html="itemDetailHtml"></div>

          <!-- 모달을 통해 상세 설명 수정 -->
          <item-create-modal
            :value="showModal"
            :detail="item.detail"
            @update:value="showModal = false"
            @update:detail="updateDetail"
          />
          <v-file-input
            v-model="item.imagePath"
            label="이미지 파일"
            accept="image/*"
          ></v-file-input>
          <v-text-field
            v-model="item.minimumStock"
            label="최소 재고"
            type="number"
            required
          ></v-text-field>
          <v-btn type="submit" color="success">등록하기</v-btn>
        </v-form>
        <item-create-modal
          :value="showModal"
          :detail="item.detail"
          @update:value="showModal = $event"
          @update:detail="item.detail = $event"
        />
      </v-container>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="primary" @click="close">닫기</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
import axios from "axios";
import ItemCreateModal from "@/components/ItemCreateModal.vue";

export default {
  components: {
    ItemCreateModal,
  },
  created() {},
  props: {
    dialog: Boolean,
  },
  emits: ["update:dialog"],
  data() {
    return {
      item: {
        name: "",
        stock: 0,
        price: 0,
        category: "",
        size: "",
        color: "",
        detail: "",
        imagePath: null,
        minimumStock: 0,
        delYN: false,
        isBaned: false,
      },
      categories: [
        "가구",
        "도서",
        "가전",
        "생활",
        "건강",
        "스포츠",
        "식품",
        "육아",
        "의류",
        "잡화",
        "화장품",
      ],
      optionList: [],
      showModal: false,
    };
  },
  watch: {
    "item.detail": function (newVal) {
      this.itemDetailHtml = newVal; // 상세 설명 업데이트 시 HTML 내용도 업데이트
    },
  },
  methods: {
    updateDialog(value) {
      this.$emit("update:dialog", value);
    },
    close() {
      this.$emit("update:dialog", false);
    },
    async submitItem() {
      try {
        const formData = new FormData();
        // Add item data
        formData.append("name", this.item.name);
        formData.append("stock", this.item.stock);
        formData.append("price", this.item.price);
        formData.append("category", this.item.category);
        formData.append("size", this.item.size);
        formData.append("color", this.item.color);
        formData.append("detail", this.item.detail);
        formData.append("minimumStock", this.item.minimumStock);
        formData.append("delYN", this.item.delYN);
        formData.append("isBaned", this.item.isBaned);

        // Add optionList as JSON string
        const blob = new Blob([JSON.stringify(this.optionList)], {
          type: "application/json",
        });
        formData.append("optionList", blob);

        // Add image file
        if (this.item.imagePath) {
          formData.append(
            "imagePath",
            this.item.imagePath[0],
            this.item.imagePath[0].name
          );
        }
        const email = localStorage.getItem("email");
        const response = await axios.post(
          `${process.env.VUE_APP_API_BASE_URL}/ortopia-item-service/item/create`,
          formData,
          {
            headers: {
              "Content-Type": "multipart/form-data",
              Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
              "X-Refresh-Token": `${localStorage.getItem("refreshToken")}`,
              myEmail: `${email}`,
            },
          }
        );

        console.log("Item registered successfully", response.data);
        alert("Item registered successfully");
        window.location.href = "/";
      } catch (error) {
        console.error("Error registering the item", error);
        alert("Error registering the item");
      }
    },
    addOption() {
      if (this.optionList.length < 3) {
        this.optionList.push({ optionName: "", details: [""] });
      } else {
        alert("옵션을 최대 3개까지만 추가할 수 있습니다.");
      }
    },
    removeOption(index) {
      this.optionList.splice(index, 1);
    },
    addOptionDetail(index) {
      // 옵션 내용이 5개 이하일 때만 추가
      if (this.optionList[index].details.length < 5) {
        this.optionList[index].details.push("");
      } else {
        alert("옵션 내용을 최대 5개까지만 추가할 수 있습니다.");
      }
    },
    removeOptionDetail(optionIndex, detailIndex) {
      this.optionList[optionIndex].details.splice(detailIndex, 1);
    },
  },
};
</script>

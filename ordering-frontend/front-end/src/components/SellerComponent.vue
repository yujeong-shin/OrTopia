<template>
  <v-container fluid style="max-width: 55vw">
    <div style="height: 50px"></div>
    <v-row>
      <v-col cols="12" md="4"> </v-col>
      <v-col cols="12" md="8">
        <v-col cols="12" class="text-right">
          <v-btn color="gray" class="mr-2" @click="showManageItemsModal = true"
            >판매물품 등록</v-btn
          >
        </v-col>
      </v-col>
    </v-row>
    <ManageItemsModal
      v-model="showManageItemsModal"
      @update:dialog="updateDialog('showManageItemsModal', $event)"
    ></ManageItemsModal>
  </v-container>
</template>

<script>
import ManageItemsModal from "@/components/ManageItemsModal.vue";
export default {
  components: {
    ManageItemsModal,
  },
  created() {},
  props: {
    dialog: Boolean,
  },
  emits: ["update:dialog"],
  data() {
    return {
      showManageItemsModal: false,
    };
  },
  methods: {
    close() {
      this.$emit("update:dialog", false);
    },
    updateDialog(modalName, newVal) {
      this[modalName] = newVal;
    },
  },
};
</script>

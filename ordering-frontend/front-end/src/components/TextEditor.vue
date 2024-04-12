<template>
  <div v-if="editor" class="d-flex justify-start align-center">
    <!-- 볼드 -->
    <button
      @click="editor.chain().focus().toggleBold().run()"
      :disabled="!editor.can().chain().focus().toggleBold().run()"
      :class="{ 'is-active': editor.isActive('bold') }" 
    >
      <v-icon color="orange">mdi-format-bold</v-icon>
    </button>

     <!-- 이미지 추가 -->
     <button @click="addImage" class="mx-2">
      <v-icon color="orange">mdi-image</v-icon>
    </button>

    <!-- 이탤릭 -->
    <button
      @click="editor.chain().focus().toggleItalic().run()"
      :disabled="!editor.can().chain().focus().toggleItalic().run()"
      :class="{ 'is-active': editor.isActive('italic') }" class="mx-2"
    >
      <v-icon color="orange">mdi-format-italic</v-icon>
    </button>

    <!-- 취소선 -->
    <button
      @click="editor.chain().focus().toggleStrike().run()"
      :disabled="!editor.can().chain().focus().toggleStrike().run()"
      :class="{ 'is-active': editor.isActive('strike') }" class="mx-2"
    >
      <v-icon color="orange">mdi-format-strikethrough-variant</v-icon>
    </button>

    <!-- 제목 1 -->
    <button
      @click="editor.chain().focus().toggleHeading({ level: 1 }).run()"
      :class="{ 'is-active': editor.isActive('heading', { level: 1 }) }" class="mx-2"
    >
      <v-icon color="orange">mdi-format-header-1</v-icon>
    </button>

    <!-- 제목 2 -->
    <button
      @click="editor.chain().focus().toggleHeading({ level: 2 }).run()"
      :class="{ 'is-active': editor.isActive('heading', { level: 2 }) }" class="mx-2"
    >
      <v-icon color="orange">mdi-format-header-2</v-icon>
    </button>

    <!-- 제목 3 -->
    <button
      @click="editor.chain().focus().toggleHeading({ level: 3 }).run()"
      :class="{ 'is-active': editor.isActive('heading', { level: 3 }) }" class="mx-2"
    >
      <v-icon color="orange">mdi-format-header-3</v-icon>
    </button>

    <!-- 실행 취소 -->
    <button
      @click="editor.chain().focus().undo().run()"
      :disabled="!editor.can().chain().focus().undo().run()" class="mx-2"
    >
      <v-icon color="orange">mdi-undo-variant</v-icon>
    </button>

    <!-- 다시 실행 -->
    <button
      @click="editor.chain().focus().redo().run()"
      :disabled="!editor.can().chain().focus().redo().run()" class="mx-2"
    >
      <v-icon color="orange">mdi-redo-variant</v-icon>
    </button>
        <!-- 리스트 추가 -->
        <button @click="editor.chain().focus().toggleBulletList().run()" :class="{ 'is-active': editor.isActive('bulletList') }" class="mx-2">
          <v-icon color="orange">mdi-format-list-bulleted</v-icon>
        </button>
    
        <button @click="editor.chain().focus().toggleOrderedList().run()" :class="{ 'is-active': editor.isActive('orderedList') }" class="mx-2">
          <v-icon color="orange">mdi-format-list-numbered</v-icon>
        </button>
    
        <!-- 인용구 -->
        <button @click="editor.chain().focus().toggleBlockquote().run()" :class="{ 'is-active': editor.isActive('blockquote') }" class="mx-2">
          <v-icon color="orange">mdi-format-quote-close</v-icon>
        </button>
    
        <!-- 코드 블록 -->
        <button @click="editor.chain().focus().toggleCodeBlock().run()" :class="{ 'is-active': editor.isActive('codeBlock') }" class="mx-2">
          <v-icon color="orange">mdi-code-tags</v-icon>
        </button>
    
        <!-- 수평선 -->
        <button @click="editor.chain().focus().setHorizontalRule().run()" class="mx-2">
          <v-icon color="orange">mdi-minus</v-icon>
        </button>
  
  </div>
  <input type="file" ref="fileInput" accept="image/*" @change="handleFileChange" style="display: none" />
  <!-- 에디터 영역 -->
  <editor-content :editor="editor" style="height: 90%" />
</template>
  
<script>
import StarterKit from "@tiptap/starter-kit";
import Image from "@tiptap/extension-image"; // Image 확장 기능 import
import { Editor, EditorContent } from "@tiptap/vue-3";
import axios from 'axios';

export default {
  components: {
    EditorContent,
  },
  props: {
    modelValue: {
      type: String,
      default: "",
    },
  },
  emits: ["update:modelValue"],
  data() {
    return {
      editor: null,
    };
  },
  mounted() {
    this.editor = new Editor({
      extensions: [
        StarterKit,
        Image,
      ],
      content: this.modelValue,
      onUpdate: ({ editor }) => {
        this.$emit("update:modelValue", editor.getHTML());
      },
    });
  },
  beforeUnmount() {
    if (this.editor) {
      this.editor.destroy();
    }
  },
  methods: {
    addImage() {
      this.$refs.fileInput.click();
    },
    handleFileChange(event) {
      const files = event.target.files;
      if (files.length === 0) {
        return;
      }
      const file = files[0];
      this.uploadImage(file);
    },
    uploadImage(file) {
      let formData = new FormData();
      formData.append('file', file); // 'file' 파라미터 이름 확인

      axios.post(`${process.env.VUE_APP_API_BASE_URL}/notice-service/upload`, formData, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('accessToken')}`,
          "X-Refresh-Token": localStorage.getItem('refreshToken'),          
          'Content-Type': 'multipart/form-data'
        }
      }).then(response => {
        const imageUrl = response.data; // 서버로부터 응답받은 이미지 URL
        this.insertImageToEditor(imageUrl);
      }).catch(error => {
        console.error('Error uploading image:', error);
      });
    },
    insertImageToEditor(imageUrl) {
      const imageHtml = `<img src="${imageUrl}" alt="Uploaded Image"/>`;
      this.editor.chain().focus().insertContent(imageHtml).run();
    },
  },
}
</script>
  
  <style lang="scss">
  .ProseMirror {
    height: 100%;
    border: 1px solid black;
    border-radius: 10px;
    padding: 6px;
    overflow: scroll;
  
    > * + * {
      margin-top: 0.75em;
    }
  
    ul,
    ol {
      padding: 0 1rem;
    }
  
    h1,
    h2,
    h3,
    h4,
    h5,
    h6 {
      line-height: 1.1;
    }
  
    code {
      background-color: rgba(#616161, 0.1);
      color: #616161;
    }
  
    pre {
      background: #0d0d0d;
      color: #fff;
      font-family: "JetBrainsMono", monospace;
      padding: 0.75rem 1rem;
      border-radius: 0.5rem;
  
      code {
        color: inherit;
        padding: 0;
        background: none;
        font-size: 0.8rem;
      }
    }
  
    img {
      max-width: 100%;
      height: auto;
    }
  
    blockquote {
      padding-left: 1rem;
      border-left: 2px solid rgba(#0d0d0d, 0.1);
    }
  
    hr {
      border: none;
      border-top: 2px solid rgba(#0d0d0d, 0.1);
      margin: 2rem 0;
    }
  }
  .ProseMirror {
    min-height: 400px; /* 최소 높이 조정 */
    font-size: 18px; /* 글자 크기 조정 */
    border: 1px solid #ccc; /* 경계선 색상을 더 부드러운 색으로 변경 */
    border-radius: 4px; /* 경계선 둥근 처리 */
    padding: 20px; /* 안쪽 여백 조정 */
    overflow: auto; /* 내용이 넘칠 경우 스크롤바 표시 */
  
    > * + * {
      margin-top: 1em;
    }
  
    ul, ol {
      padding: 0 1rem;
    }
  
    /* 기타 스타일 조정 */
  }
  </style>
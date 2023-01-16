<script setup lang="ts">
import {onMounted, ref} from "vue";

let webSocket: WebSocket;
const message = ref('');
const messages = ref<message[]>([]);
type message = {
  message: string;
  host: string;
  isOwner: boolean;
}
const onMessage = (event: MessageEvent) => {
  messages.value.push(JSON.parse(event.data));
}
const onOpen = () => {
  console.log("连接成功")
}
const onError = () => {
  console.log("连接失败")
}
const onClose = () => {
  console.log("连接关闭")
}
const sendMessage = () => {
  webSocket.send(message.value);
  message.value = '';
}
onMounted(() => {
  if (webSocket) webSocket.close();
  init()
})
const init = () => {
  webSocket = new WebSocket("ws://192.168.2.92:8080/chatroom")
  webSocket.onmessage = onMessage
  webSocket.onopen = onOpen
  webSocket.onerror = onError
  webSocket.onclose = onClose
}
const errorMessage = ref("")
// 回车发送消息
const onEnter = (e: KeyboardEvent) => {
  if (e.key === 'Enter') {
    if (message.value && message.value.trim()) {
      errorMessage.value = ""
      sendMessage()
    } else {
      errorMessage.value = "傻鸟没消息别tm按了！！！"
    }
  }
}
// 监听键盘事件
window.addEventListener('keydown', onEnter)
</script>

<template>
  <div class="messageBox">
    <div v-for="(v,i) in messages" class="box">
      <div v-if="!v.isOwner" class="other">
        {{ '来自[' + v.host + "]说: " + v.message }}
      </div>
      <div v-else class="owner">
        {{ '我说: ' + v.message }}
      </div>
    </div>
  </div>
  <div>
    <button @click="init">重连</button>
  </div>
  <input type="text" v-model="message" @change="onEnter">
  <div style="color: #f00">{{ errorMessage }}</div>
</template>

<style scoped>
.box {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  margin: 10px;
  overflow: auto;
  height: 24px;
}

.other {
  display: block;
  text-align: left;
}

.owner {
  display: block;
  text-align: right;
}

.messageBox {
  max-height: 800px;
  overflow: auto;
}
</style>

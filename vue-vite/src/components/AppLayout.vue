<template>
  <div class="layout">
    <AppNavbar />
    <main class="main-content">
      <slot />
    </main>
  </div>
</template>

<script>
import AppNavbar from './AppNavbar.vue'

export default {
  name: 'AppLayout',
  components: {
    AppNavbar
  }
}
</script>

<style scoped>
.layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.main-content {
  flex: 1;
  background-color: #f5f5f5;
}
</style> 
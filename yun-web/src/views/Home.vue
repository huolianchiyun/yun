<template>
  <div class="home-container">
    <div class="home-editor-container">
    ***************首页**************
    </div>
  </div>
</template>
<script>
export default {
  name: 'Home'
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .home-editor-container {
    padding: 32px;
    background-color: rgb(240, 242, 245);
    position: relative;
  }
  @media (max-width: 1024px) {
    .chart-wrapper {
      padding: 8px;
    }
  }
</style>

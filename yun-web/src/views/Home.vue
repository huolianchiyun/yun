<template>
  <div class="dashboard-container">
    <div class="dashboard-editor-container">
      <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
      dd
    </div>
  </div>
</template>
<script>
export default {
  name: 'Home',
  async created () {
    const { data: res } = await this.$http.get('menu/user')
    console.log(res)
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .dashboard-editor-container {
    padding: 32px;
    background-color: rgb(240, 242, 245);
    position: relative;

    .github-corner {
      position: absolute;
      top: 0;
      border: 0;
      right: 0;
    }

    .chart-wrapper {
      background: #fff;
      padding: 16px 16px 0;
      margin-bottom: 32px;
    }
  }

  @media (max-width:1024px) {
    .chart-wrapper {
      padding: 8px;
    }
  }
</style>

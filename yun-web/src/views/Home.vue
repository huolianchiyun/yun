<template>
  <div class="home-container">
    <div class="home-editor-container">
    ***************首页**************
    </div>
    <div>
      <el-button type="info" @click="doExport">下载</el-button>
    </div>
  </div>
</template>
<script>

import { download } from '@/api/data'
import { downloadFile } from '@/utils'

export default {
  name: 'Home',
  methods: {
    doExport () {
      // eslint-disable-next-line no-undef
      download('/user/download', {}).then(result => {
        // eslint-disable-next-line no-undef
        downloadFile(result, '数据', 'xlsx')
      }).catch(() => {
      })
    }
  }
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

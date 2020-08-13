
数据权限

缓存spring、redis

可插拔redis

去掉role、role_menu表原因：
1、之前group关联role，role关联menu，但group具有层级结构，要求子组权限不能父组权限，
若group->role->menu，将导致group层级设权限很难控制，因为权限被role封装了，用户不好设置，
故让group直接关联->menu，这样role就多余了，不能让多个入口和menu关联，这样容易造成权限控制混乱。
故让group代替role且使其具备层级概念。


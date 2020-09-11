local cursor = 0
local keyNum = 0
repeat
    --- KEYS[1]:获取key, ARGV[1]:获取一个参数
   local res = redis.call('scan',cursor,'MATCH',KEYS[1],'COUNT',ARGV[1])
   if(res ~= nil and #res>=0)
   then
      cursor = tonumber(res[1])
      local ks = res[2]
      if(ks ~= nil and #ks>0)
      then
         for i=1,#ks,1 do
            local key = tostring(ks[i])
            redis.call('UNLINK',key)
         end
         keyNum = keyNum + #ks
      end
     end
until( cursor <= 0 )
return keyNum

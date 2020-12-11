package yun.admincenter.store;

import java.io.InputStream;

/**
 * 文件存取操作接口
 */
public interface IStoreOperator {

    /**
     * 上传文件至存储系统
     * @param inputStream  源文件流
     * @param filename  文件名称
     * @return  文件存储路径
     */
    String upload(InputStream inputStream, String filename);

    /**
     * 上传指点路径下的文件至存储系统
     * @param filepath  源文件路径
     * @return 文件存储地址
     */
    String upload(String filepath);

    /**
     * 从存储系统下载文件
     * @param filePath 文件在存储系统中的存储地址
     * @param destPath 文件将要下载到的地址
     * @return 下载是否成功
     */
    boolean download(String filePath, String destPath);

    /**
     * 从存储系统下载文件
     * @param filePath  文件在存储系统中的存储地址
     * @return 文件内容字节数组
     */
    byte[] download(String filePath);

    /**
     *  从存储系统中删除文件
     * @param filePath  文件在存储系统中的存储地址
     * @return  是否删除成功
     */
    boolean delete(String filePath);

}

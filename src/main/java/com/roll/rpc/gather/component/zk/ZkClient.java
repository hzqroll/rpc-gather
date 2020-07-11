package com.roll.rpc.gather.component.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义zk客户端
 * <p>created on 2020/7/8 9:07 上午
 */
@Service
public class ZkClient {

    private final CuratorFramework curatorFramework;

    public ZkClient(CuratorFramework curatorFramework) {
        this.curatorFramework = curatorFramework;
    }

    /**
     * 启动链接
     */
    public void start() {
        curatorFramework.start();
    }

    /**
     * 创建节点
     *
     * @param path       节点路径
     * @param data       节点数据
     * @param createMode 节点模式：{@link CreateMode}
     * @return 节点路径
     * @throws Exception 不存在则抛出NoNodeException
     */
    public String create(String path, byte[] data, CreateMode createMode) throws Exception {
        return curatorFramework.create().withMode(createMode).forPath(path, data);
    }

    /**
     * 获取节点内容
     *
     * @param path 文件路径
     * @return 节点内容
     * @throws Exception 不存在则抛出NoNodeException
     */
    public String get(String path) throws Exception {
        return new String(curatorFramework.getData().forPath(path));
    }

    /**
     * 创建节点
     *
     * @param path       节点路径
     * @param data       节点数据
     * @param createMode 节点模式：{@link CreateMode}
     * @return 节点状态
     * @throws Exception 不存在则抛出NoNodeException
     */
    public Stat update(String path, byte[] data, CreateMode createMode) throws Exception {
        return curatorFramework.setData().forPath(path, data);
    }

    /**
     * 删除节点
     *
     * @param path 节点路径
     * @throws Exception 不存在则抛出NoNodeException
     */
    public void delete(String path) throws Exception {
        curatorFramework.delete().forPath(path);
    }

    /**
     * 检查节点信息
     *
     * @param path 路径
     * @return 节点信息
     */
    public boolean exist(String path) {
        try {
            Stat stat = curatorFramework.checkExists().forPath(path);
            return stat != null;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 返回指定路径下面的子节点列表。
     *
     * @param path 路径
     * @return 列表
     */
    public List<String> getChild(String path) {
        try {
            return curatorFramework.getChildren().forPath(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}

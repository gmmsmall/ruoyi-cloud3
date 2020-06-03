package com.ruoyi.system.util;


import com.ruoyi.system.domain.TokenTree;
import com.ruoyi.system.result.TokenTreeResult;
import io.netty.util.internal.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class TokenTreeUtil {

    private final static String TOP_NODE_NO = "0";

    public static <T> TokenTree<T> build(List<TokenTree<T>> nodes) {
        if (nodes == null) {
            return null;
        }
        List<TokenTree<T>> topNodes = new ArrayList<>();
        nodes.forEach(node -> {
            String pNo = node.getParentNo();
            if (pNo == null || TOP_NODE_NO.equals(pNo.trim()) || StringUtil.isNullOrEmpty(pNo.trim())) {
                topNodes.add(node);
                return;
            }
            for (TokenTree<T> n : nodes) {
                String tokenNo = n.getTokenNo();
                if (tokenNo != null && tokenNo.equals(pNo)) {
                    if (n.getChildren() == null)
                        n.initChildren();
                    n.getChildren().add(node);
                    node.setHasParent(true);
                    n.setHasChildren(true);
                    n.setHasParent(true);
                    return;
                }
            }
            if (topNodes.isEmpty())
                topNodes.add(node);
        });
        TokenTree<T> root = new TokenTree<>();
        root.setTokenNo("");
        root.setParentNo("");
        root.setHasParent(false);
        root.setHasChildren(true);
        root.setChildren(topNodes);
        return root;
    }

    public static <T> TokenTreeResult<T> buildResult(List<TokenTreeResult<T>> nodes) {
        if (nodes == null) {
            return null;
        }
        List<TokenTreeResult<T>> topNodes = new ArrayList<>();
        nodes.forEach(node -> {
            String pNo = node.getParentNo();
            if (pNo == null || TOP_NODE_NO.equals(pNo.trim()) || StringUtil.isNullOrEmpty(pNo.trim())) {
                topNodes.add(node);
                return;
            }
            for (TokenTreeResult<T> n : nodes) {
                String tokenNo = n.getTokenNo();
                if (tokenNo != null && tokenNo.equals(pNo)) {
                    if (n.getChildren() == null)
                        n.initChildren();
                    n.getChildren().add(node);
                    node.setHasParent(true);
                    n.setHasChildren(true);
                    n.setHasParent(true);
                    return;
                }
            }
            if (topNodes.isEmpty())
                topNodes.add(node);
        });
        TokenTreeResult<T> root = new TokenTreeResult<>();
        root.setTokenNo("");
        root.setParentNo("");
        root.setHasParent(false);
        root.setHasChildren(true);
        root.setChildren(topNodes);
        return root;
    }

    protected TokenTreeUtil() {
    }
}

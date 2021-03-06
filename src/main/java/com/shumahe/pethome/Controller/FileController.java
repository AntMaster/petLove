package com.shumahe.pethome.Controller;

import com.shumahe.pethome.Domain.UserPetAlbum;
import com.shumahe.pethome.Domain.UserPetPhoto;
import com.shumahe.pethome.Enums.ResultEnum;
import com.shumahe.pethome.Exception.PetHomeException;
import com.shumahe.pethome.Repository.UserPetAlbumRepository;
import com.shumahe.pethome.Repository.UserPetPhotoRepository;
import com.shumahe.pethome.Service.PetService;
import com.shumahe.pethome.Util.FileUtil;
import com.shumahe.pethome.Util.ResultVOUtil;
import com.shumahe.pethome.VO.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import java.io.*;

@RestController
@Slf4j
@RequestMapping("/upload")
public class FileController {


    //request.getRealPath() 过时
    /*request.getSession().getServletContext().getRealPath("/")
      在Servlet 里用this.getServletContext().getRealPath("/");获得绝对路径。
        struts里用this.getServlet().getServletContext().getRealPath("/")获得绝对路径。
    */


    @Autowired
    UserPetAlbumRepository userPetAlbumRepository;

    @Autowired
    UserPetPhotoRepository userPetPhotoRepository;
    /**
     * 发布上传图片
     *
     * @param file
     * @param request
     * @return
     */

    @Value("${web.upload-path}")
    private String picturePath;

    /**
     * 发布上传图片
     *
     * @param file
     * @param
     * @return
     */
    @PutMapping(value = "/publish")
    public ResultVO upload(@RequestParam("file") MultipartFile file) {

        String fileName = (System.currentTimeMillis() + file.getOriginalFilename()).replaceAll(";", "");
        String filePath = picturePath + File.separator + "picture" + File.separator + "publish" + File.separator;
        try {
            FileUtil.uploadFile(file.getBytes(), filePath, fileName);
        } catch (Exception e) {
            throw new PetHomeException(ResultEnum.FAILURE.getCode(), "文件上传失败");
        }

        String path = "upload/picture/publish/" + fileName;
        return ResultVOUtil.success(path);
    }


    /**
     * 发布上传图片
     *
     * @param file
     * @param
     * @return
     */
    @PutMapping(value = "/auth")
    public ResultVO authUpload(@RequestParam("file") MultipartFile file) {

        String fileName = (System.currentTimeMillis() + file.getOriginalFilename()).replaceAll(";", "");
        String filePath = picturePath + File.separator + "picture" + File.separator + "auth" + File.separator;
        try {
            FileUtil.uploadFile(file.getBytes(), filePath, fileName);
        } catch (Exception e) {
            throw new PetHomeException(ResultEnum.FAILURE.getCode(), "文件上传失败");
        }

        String path = "upload/picture/auth/" + fileName;
        return ResultVOUtil.success(path);
    }


    /**
     * 宠卡上传头像
     *
     * @param file
     * @param
     * @return
     */
    @PutMapping(value = "/pet")
    public ResultVO petUpload(@RequestParam("file") MultipartFile file) {

        String fileName = (System.currentTimeMillis() + file.getOriginalFilename()).replaceAll(";", "");
        String filePath = picturePath + File.separator + "picture" + File.separator + "pet" + File.separator;
        try {
            FileUtil.uploadFile(file.getBytes(), filePath, fileName);
        } catch (Exception e) {
            throw new PetHomeException(ResultEnum.FAILURE.getCode(), "文件上传失败");
        }

        String path = "upload/picture/pet/" + fileName;
        return ResultVOUtil.success(path);
    }


    @PutMapping(value = "/pettest")
    public ResultVO petUploadTest(@RequestBody String imgStr) {


        String encode = imgStr.split("data:image/jpeg;base64,")[1];

        BASE64Decoder decoder = new BASE64Decoder();
        // 解密
        byte[] b;
        String path = "";

        try {
            b = decoder.decodeBuffer(imgStr);
            // 处理数据
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }

            String fileName = System.currentTimeMillis() + "test.jpg";
            String filePath = picturePath + File.separator + "picture" + File.separator + "pet" + File.separator;


            OutputStream out = new FileOutputStream(filePath + fileName);
            out.write(b);
            out.flush();
            out.close();

            path = "upload/picture/pet/" + fileName;


        } catch (IOException e) {
            e.printStackTrace();
        }

        return ResultVOUtil.success(path);

         /*   String fileName = (System.currentTimeMillis() + file.getOriginalFilename()).replaceAll(";", "");
            String filePath = picturePath + File.separator + "picture" + File.separator + "pet" + File.separator;
            try {
                FileUtil.uploadFile(file.getBytes(), filePath, fileName);
            } catch (Exception e) {
                throw new PetHomeException(ResultEnum.FAILURE.getCode(), "文件上传失败");
            }

            String path = "upload/picture/pet/" + fileName;*/


    }

    /**

     * @return
     * @Description: 将base64编码字符串转换为图片
     * @Author:
     * @CreateTime:
     */
        /*
        public static boolean generateImage(String imgStr, String path){

            if (imgStr == null)
                return false;

            BASE64Decoder decoder = new BASE64Decoder();
            try {
                // 解密
                byte[] b = decoder.decodeBuffer(imgStr);
                // 处理数据
                for (int i = 0; i < b.length; ++i) {
                    if (b[i] < 0) {
                        b[i] += 256;
                    }
                }
                OutputStream out = new FileOutputStream(path);
                out.write(b);
                out.flush();
                out.close();
                return true;
            } catch (Exception e) {
                return false;
            }
       }
*/

    /**
     * 相册上传图片
     *
     * @param file
     * @param
     * @return
     */
    @PutMapping(value = "/album")
    public ResultVO photoUpload(@RequestParam("file") MultipartFile file, @RequestParam("albumid") Integer albumid) {

        String fileName = (System.currentTimeMillis() + file.getOriginalFilename()).replaceAll(";", "");
        String filePath = picturePath + File.separator + "picture" + File.separator + "album" + File.separator;
        try {
            FileUtil.uploadFile(file.getBytes(), filePath, fileName);
        } catch (Exception e) {
            throw new PetHomeException(ResultEnum.FAILURE.getCode(), "文件上传失败");
        }

        UserPetAlbum album = userPetAlbumRepository.findOne(albumid);
        if (album == null) {
            throw new PetHomeException(ResultEnum.FAILURE.getCode(), "宠物相册不存在");
        }

        String photoPath = "upload/picture/album/" + fileName;
        UserPetPhoto petPhoto = new UserPetPhoto();
        petPhoto.setPetId(album.getPetId());
        petPhoto.setAlbumId(albumid);
        petPhoto.setPath(photoPath);

        UserPetPhoto save = userPetPhotoRepository.save(petPhoto);

        /**
         * 默认第一张图片做为封面
         */
        String coverPath = album.getCoverPath();
        if (coverPath == null) {
            album.setCoverPath(save.getPath());
            userPetAlbumRepository.save(album);
        }

        return ResultVOUtil.success(save);
    }


  /*  @PutMapping(value = "/pet")
    public ResultVO petUpload(@RequestParam("file") MultipartFile file) {

        String fileName = (System.currentTimeMillis() + file.getOriginalFilename()).replaceAll(";", "");
        //String filePath = request.getSession().getServletContext().getRealPath("")  + "WEB-INF" + File.separator + "classes" + File.separator;
        String filePath =  picturePath + File.separator + "picture" + File.separator + "publish" +  File.separator  ;
        try {
            FileUtil.uploadFile(file.getBytes(), filePath, fileName);
        } catch (Exception e) {
            throw new PetHomeException(ResultEnum.FAILURE.getCode(), "文件上传失败");
        }

        String path  = "upload/picture/pet/" + fileName;
        return ResultVOUtil.success(path);
    }
*/


}

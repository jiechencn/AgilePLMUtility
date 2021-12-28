package zigzagsoft.agile.util;

import Acme.Crypto.BlowfishCipher;

import java.io.UnsupportedEncodingException;

/**
 * Created by jijichen on 7/4/2014.
 */
public class ED {

        protected static String[][] bf_key = { { "D1F6", "180CAEC48466592A91A2FA8981485288" }, { "95FB", "1D19E4C18E05C25A7181CF75DA3D2565" }, { "098D", "3449BA203742286594B6B56C51EBDD51" }, { "A838", "037B4D3DB8712D059B6919A8C4E3ECE3" }, { "2420", "9E3C1D8DB83F3D21D7A74E97C11C14AC" }, { "7AD5", "AD772EBAB34492D94C1F535F49EBD1E5" }, { "F0A6", "B5058C0FD6A646FED16C89E8445873BA" }, { "22DF", "B40DBF3495137B35861079F4D28CCC35" }, { "FD6E", "4230FB87AEC2ADA7D0F147A889F2A66F" }, { "22C1", "594A380A8731C3AD3AD10E18BB08F173" }, { "0FD2", "A30BA098919BAB1940E35EC958FAA17E" }, { "89B9", "741315352164C60DE123D4DA36A8F264" }, { "1584", "E26AFE6A3E2574B6A282B3C904C34074" }, { "221B", "C3230CE437D5717076FF6E4D60612772" }, { "8BFA", "C341D2DB381564C5423A41E11447BC36" }, { "8E01", "91C5F4CC52400C07B13F2F77698A56A9" }, { "739F", "772AB59187FC29EA0C6EDBF6AFC1C2C1" }, { "BB4A", "6351C25868C878024557EFAA2E31D08F" }, { "AB40", "D420BAA544049EA772D44D7F42D52E5A" }, { "45C1", "A7CC6FC3C4DE545DC4828E2F845B4D12" }, { "3878", "0C4140E54EBD9A06531E96704DFD2561" }, { "056B", "16CBC4155CB9EE0C2894A8C9CB4BD5DF" }, { "B18C", "AD594FC7B2F95962B87028FD7590CFC7" }, { "5700", "68BF35A28A112135AB6D887B6ED7D455" }, { "80AF", "6D200320F071E8D97288BA721E31C86B" }, { "17D4", "C07B3C10451F2C8AEB18D0FDAA883B3A" }, { "0E40", "2958CCE34D74B1694DAD35DC2CA1A4AF" }, { "ECE6", "209802FC13E7A8B61BC8048E8C1A5A4B" }, { "1DC5", "A43FFD58AE755E894332926A6F849C31" }, { "2497", "BE009AE91F1993BD88AC9F08797DB301" }, { "CF7A", "DFC6566846263DACFEC5E8BD97C4F486" }, { "E51B", "562D1522614D6C17CC819F03EB378073" }, { "A6A8", "7A8D33B39D572ABDDF3262D28B3B9558" }, { "4EE8", "A0ED34772A841D9B0D4848D1FB4FF635" }, { "3600", "4EA0DA2B60D12F00B11B271438E3D32D" }, { "BA45", "82EF58A6BE7627ED260BF49CF8365175" }, { "D9BD", "DF9897EC8F9AAD464158E15F3D481D77" }, { "B79C", "F46E34C1E33FABA46559A6552456C218" }, { "517E", "EDDBF926C6AE83210E7BCA60448F38A9" }, { "881B", "24B20BBD257CFAF6CAFDD710F2B09E9F" }, { "9BBC", "E024E9385FE2BEDC950516004FCABDAD" }, { "57B0", "C89A07F79470133FE0EFDCE03008C24A" }, { "C861", "23611C3A708BDA990B65653DBAA16174" }, { "A0C6", "A383439EF93A6F73132D03D946126438" }, { "658D", "178D871083D7DE487540CF2B4FFF9A71" }, { "EBAD", "3D0CC25E3017718632CFCD8C92824549" }, { "F733", "64224C2BEEEC1D8267107F986516C4E8" }, { "0256", "2F12C4524BB22D1C67B59D2373186D0C" }, { "9CE3", "6B924EB732DF35E8A322F2AA0D49D654" }, { "54A0", "0DF21375ED27A3A4D56EA0E4919DA009" }, { "ECA1", "F63E1534C9CD7626632D71142B531F0A" }, { "3953", "675470680FEE579541A892F3032CD316" }, { "DB16", "C9C72AC29D0ABF955EE97CA24C2CB4F0" }, { "07D2", "0B69C3458C9ECF9ECDC1E42DE410CEF2" }, { "F1F6", "C2691CAB75F035DDCA0B93D588C0E0B6" }, { "1D28", "88F5E39B3DBC1B8E9D994A163758608A" }, { "D82D", "10B895784EDA8F0F8ABEC4C0DB97128A" }, { "39DB", "E3732B87235CDE6EC6E9E9935744ADC8" }, { "4F77", "576FD86F34F8454A7B737703D8173390" }, { "AB64", "E26435FBF6CB50BC0FBDDB9B92C6828C" }, { "4839", "2DDE42B8A29493C1C21E96C0C3EF908F" }, { "C49F", "97873B7CA110E0ED31FC265694DA6BC0" }, { "057C", "438C2DC5181892A6EB7042ACA5541C68" }, { "5E12", "93313774327BB5CD691A3DE573D00B11" }, { "35DD", "01286175BE8E7557B47D8E6AFA3D6F36" }, { "6370", "4474C8DD22FF20DA425495630365565C" }, { "E0C3", "259246B7D781376720DEC24874C1329D" }, { "3742", "70F62BE56D6C9FEB43D82905BCE896BF" }, { "2962", "73E11CF619CE3E8C1D2AD4FA4CB83716" }, { "7869", "CA81ACBE6C04A311106D945079B043F4" }, { "BE6B", "B69985F2EACA7257E763DEE206A62092" }, { "7C22", "9175856CDA4AF7963413648A0096AA02" }, { "A6E0", "05284FC0E481D1E842F3FD8EAD991917" }, { "D60C", "C4CB0B336D8EC7385A279D0C23DBCB29" }, { "7F6E", "C00C2AA7FD9A0F8EE685257599233CA4" }, { "AD96", "1B7081A65EF4BE5D6B38A6299EEDC7E9" }, { "4566", "36913EC49D01A420FA5BB69D597EA7ED" }, { "8CA7", "1400B93F187F0EC9814E48B77AC64359" }, { "118A", "19BDDE16C8137EB13D5B64624787ADA6" }, { "02C6", "4023B36388C788DD79F5E0A670E69E23" }, { "0012", "2C797030655A8DB1F8E852DDBADA73FE" }, { "D3AF", "E7F36381FB63C4F9AB8E0C4DAF4D3A6E" }, { "947D", "38104C2C30860A272EC7B0589894D3F6" }, { "6F1A", "EF8E70E3F37EAD315ED9F6186914C3CF" }, { "9F45", "0CAFDCF1732F02A58BB9BEF404B106A8" }, { "6C9B", "F8DC3EBA3008941859B8C75508C351DE" }, { "3074", "FEE356079B5990DCD8E0A20D9CBAAC7F" }, { "EFC0", "29861C4BCF68E69A42E2259AD9F06B09" }, { "740D", "2517BEA935C78E1BED6C420387C66BC8" }, { "82A4", "09ED17294901EA21E4F6CB6A93EC513E" }, { "1658", "D32C57D7C4EDF9F365BAECC88778E862" }, { "ADB8", "2ED6F529D68C0A438AF552991E8D8E80" }, { "E085", "89EC4575551A0BBD82D1D4187DFB5D8F" }, { "CE13", "0A8D8E5C10931F6B206D2D70F9E06791" }, { "4DB0", "8879590D850A1EB167A922B73FCBA345" }, { "0C16", "63EB492D23A94E7D8EEF7365DD4CBA3F" }, { "A7A5", "18443665994569C067145CAEF7742060" }, { "836C", "B4717994AA0586359371D2CCC759B9B1" }, { "0312", "E21AFEAA56F3D588D7D2CEC0D2A55C31" }, { "974C", "9C762F41EBEE7E6D69954E33540B4809" }, { "4913", "0DF96A9E0F33EB0BE9B1FC2222623817" }, { "688B", "BB1D3F0AA52B35B22498142A6A1B4209" }, { "DFA2", "66460A34CB5B8BDCD112C28516DDCCAB" }, { "BDF8", "A2D3260414A6D94897CD289432142374" }, { "C22A", "AD50E79402D01B7973532909C759366B" }, { "ADF7", "3A38A1AF2659BCDA8832EF618419BE4B" }, { "7433", "6B21D8E157E07EDE71145D3D6A165D60" }, { "A6A0", "337E5BD7DB8EF80844865DC8270E6BB1" }, { "B02B", "90381EDD2E94FA0594C61E70F2AEC17F" }, { "049F", "1539E8940E47249E69BA0987B69C4F7B" }, { "2626", "C634F74B80BE87510C09A42A59B94BC8" }, { "AD19", "159BA15074F155321466BC87E6D7C5D7" }, { "CB06", "960813B966509629110C4D603A0A6C30" }, { "5280", "9E5397B48052B8CF643A2845350A766F" }, { "9B39", "F83096409BDA6CA13EEE09FFDB4F470E" }, { "1909", "FEB44AF7B7F4BF0A6CCF33F484808466" }, { "18F6", "EF0C584D9E3C3617FEA4787BE63218E4" }, { "844D", "E87EEE6AC7B71D13FB415F03B8E1484C" }, { "DF04", "785F575F3E4B8556F56753C805AF736E" }, { "A8BD", "E36640724BEE0710B3C505D4C58FE119" }, { "AFD9", "EAC1080A6C16F838750A2AE310ED4E7C" }, { "FF7E", "76D53396F958D674F8CD1AC89873B461" }, { "C518", "28C6744A46236075FB2B916EC59B4CD5" }, { "04AE", "96D92A9C04E6600E42FF2548794582B9" }, { "F309", "F8081AD396F9D0822F46D76794926323" }, { "38F2", "FBA2D26D3D0D418AD156B3E99AF63384" }, { "2BCC", "5629B779AF1378643FFDFB550519C41C" }, { "C10C", "838FF227C2D6DB426E425064C76839BC" }, { "D6A8", "26704F08ECA49687A5A107A32F44A023" }, { "F633", "079F4AE9EECAD7A0B0F792FFB22CBD79" }, { "31C0", "19DC43262FEB6AF76445041B2A43E31A" }, { "8844", "895305DFD3A96BA124C3E107E20D8949" }, { "5E25", "6DEE8CF2378B6CCFC5C98AF0B1909EB7" }, { "6622", "024CF76C6C0CD086792BA0D66039D9E2" }, { "AC55", "C0AB15049E2A5C01499A9C3637822A9A" }, { "8F65", "EF15DC0102651FC7E926C22BBCD51EBF" }, { "DBF8", "AC735578EFD322A16F0AD8AD632B7D43" }, { "B9F7", "304963DB9222E9783F423F724A9DB45E" }, { "1C5A", "B30C9FB754B0A1B907BB93FD3B0B44DF" }, { "61C7", "95F525CBF4E739B9858079F6014A962D" }, { "0A6E", "4B7D61FF847C862EB2930633D8284594" }, { "7088", "63A1FF89452577EA48792E5FAB0F1E80" }, { "3F37", "22D4DEBBF62763C852E2FF085B6850CD" }, { "A58A", "127A0C1A103BC3202530A76E35921CAA" }, { "C3F6", "27055715E6420A0C57FFFFFA1F029B2B" }, { "A52C", "D9C9DDF2CDBDE764A673C64FD6F2F761" }, { "66C9", "577EA02B6A9C81AC3C39B4E7C579C096" }, { "551E", "4B8A56F00DEC603D39ED1C14DD4651C7" }, { "EC2B", "197E6EA161288E72A46DAFB41D1E824B" }, { "0D2D", "64F93F4BDA73B4CC582BD5A9220AB6D1" }, { "651A", "B4A4FA2C3C131294C4F4FFA708B94520" }, { "2283", "8261475AF2FD27A4705C92156C2D803F" }, { "0105", "E3E845B7B15BE169805AA956F139B170" }, { "1C86", "A5CC368E425E8ECB37370D1969FB2944" }, { "0043", "FEB85D4D849F1A0B67E596071AD8E582" }, { "1F14", "A841A5663A90F1B4E4110DF34F09CE35" }, { "4787", "C2922185E4E31415DAA565DEF792C1F7" }, { "31B1", "95F19F1E055E815766A842620F6CE93A" }, { "753F", "77EC56854869D80F49944A034143BFBF" }, { "CCB1", "A2C209EC5C1BCE1CFEA40A4F7CDB99AF" }, { "ED5F", "2F2C5284E63F06DE120C7FC635240376" }, { "F211", "110EB396A4031F029E640815C9F5F51A" }, { "01A6", "2AE4B5AFB99B8283A072530AF9789526" }, { "5122", "C0BECCE518E00DE5C180BE412185F87E" }, { "1213", "A9B8A588F7ADAEA3B4616C3CCC56DD32" }, { "2240", "3F294E1366E33F29900A06C38199A549" }, { "D44E", "F3F269362521233F2529C88EAA81626A" }, { "CC6B", "786D28B27A795FDB9A7ECA4760658CE9" }, { "FDAD", "C570DABBD7537A005ACAF25F338A68B5" }, { "283E", "9B8750530376FB49843B67A14D0896DF" }, { "0389", "0F24EED773D1F2173553EE2484ABE77F" }, { "DBF9", "9CDF0F3DB16755D0589C1BD74F00DAA0" }, { "3DB9", "285180F92E27534002D520AEEA964689" }, { "1447", "BAF2DD2E0E60DE42642E1DE97325C3AB" }, { "BCE1", "6CD0FDF5A869D07AF0AF460D7F766710" }, { "0394", "0188BDD18FD86A4322064F62744FDCF0" }, { "2730", "6A4B83BF292A35E3E0C8A2CD384BF944" }, { "6F24", "25620D1ADC85D16EAAC7F08652DA95D5" }, { "08DC", "816F765F64008FE37AEF3C40AEB6E8B6" }, { "79F4", "AB7E1F8CA571ACA80E0421335C8557F7" }, { "321A", "76F3697F30CD3DCA3786DF7CAAB36D33" }, { "3C56", "DA272111E2848E4EC01E27C8D7E86F4A" }, { "A8E4", "33472F86F5CDF35EF74BA49C2EEEA073" }, { "4126", "5F74A0C47D979EE2809AB13262A79CBB" }, { "3AF9", "645023E01D38545312D3C5CF38D6274F" }, { "7E93", "A1203AD7D14117BB28B2E3895130D4A1" }, { "EC87", "29140E9970BFA797617E8BE53E0CEEC0" }, { "6F94", "66049CF6751BBCE45302027FB1E6BDD7" }, { "F4FE", "ED231D4C335B3CBD81C460402762F04F" }, { "2662", "F73E325591B0E921FFE2D0FDD996B0D4" }, { "E314", "740AA98438ACC9888E8CA54D630A998B" }, { "A0FE", "D35507BFFBEB66A9550C9AD10922B8FF" }, { "BA3E", "7A42DF56F55A57BDB52EF15CB2227F30" }, { "E6F4", "096C841611ED9B37812E5FCE4A8DDDFD" }, { "96B3", "0BF23CEBCCB95C251B1E306E01BB112D" }, { "7539", "73FFC003B0A15CCC6D57533320947E03" }, { "40EA", "BF356E6C8C0ED223A4A8F70BAF2FF2C1" }, { "9DD5", "A61CF1B985EE1B5248EADD572253BE03" }, { "7C29", "C6C810DFF6F18A9BE6EEDCB901CF083D" }, { "87B0", "412BE322DF17CC73FBCC69CA240062EB" } };
        protected static final int MAX_HASH = 200;
        private static final char _padChar = '\000';
        static final String s_hexset = "0123456789ABCDEF";

        public static String encrypt(String plain)
        {
            int index = (int)(Math.random() * 200.0D);
            String encoded = BF_cfb64_encrypt(bf_key[index][1], plain);
            return bf_key[index][0] + encoded;
        }

        public static String decrypt(String cipher)
        {
            if (cipher == null) {
                return null;
            }
            if (cipher.equals("")) {
                return cipher;
            }
            String hash = cipher.substring(0, 4);
            if (hash.length() < 4) {
                return "";
            }
            for (int i = 0; i < 200; i++) {
                if (bf_key[i][0].equals(hash))
                {
                    String decoded = BF_cfb64_decrypt(bf_key[i][1], cipher.substring(4));
                    return decoded;
                }
            }
            return "";
        }

        private static String BF_cfb64_encrypt(String key, String plain)
        {
            byte[] ivec = new byte[cbc_iv.length];
            for (int i = 0; i < ivec.length; i++) {
                ivec[i] = cbc_iv[i];
            }
            byte[] bytes;
            try
            {
                bytes = plain.getBytes("UTF-8");
            }
            catch (UnsupportedEncodingException e)
            {
                bytes = plain.getBytes();
            }
            byte[] out = new byte[bytes.length];
            BF_cfb64_encrypt(bytes, out, bytes.length, key.getBytes(), ivec, 0, true);
            return convertByteToString(out);
        }

        private static String BF_encrypt(String key, String plain)
        {
            BlowfishCipher encoder = new BlowfishCipher(key.getBytes());





            byte[] bCookie = plain.getBytes();
            byte[] bufIn = new byte[8];
            byte[] bufOut = new byte[8];
            StringBuffer encoded = new StringBuffer();


            int nTimes = bCookie.length / 8 + 1;
            int nLen = bCookie.length;
            for (int i = 0; (i < nTimes) && (nLen > 0); i++)
            {
                for (int j = 0; j < 8; j++)
                {
                    int index = i * 8 + j;
                    bufIn[j] = ((byte)(j < nLen ? bCookie[(i * 8 + j)] : 0));
                }
                encoder.encrypt(bufIn, 0, bufOut, 0);
                encoded.append(convertByteToString(bufOut));
                nLen -= 8;
            }
            return encoded.toString();
        }

        private static final int hexNibbleToInt(char c)
        {
            if ((c >= '0') && (c <= '9')) {
                return c - '0';
            }
            if ((c >= 'a') && (c <= 'f')) {
                return '\n' + c - 97;
            }
            if ((c >= 'A') && (c <= 'F')) {
                return '\n' + c - 65;
            }
            return 0;
        }

        private static String BF_cfb64_decrypt(String key, String encoded)
        {
            byte[] ivec = new byte[cbc_iv.length];
            for (int i = 0; i < ivec.length; i++) {
                ivec[i] = cbc_iv[i];
            }
            byte[] in = convertStringToByte(encoded);
            byte[] out = new byte[in.length];
            BF_cfb64_encrypt(in, out, in.length, key.getBytes(), ivec, 0, false);
            try
            {
                return new String(out, "UTF-8");
            }
            catch (UnsupportedEncodingException e) {}
            return new String(out);
        }

        private static String BF_decrypt(String key, String encoded)
        {
            BlowfishCipher decoder = new BlowfishCipher(key.getBytes());

            byte[] bufIn = new byte[8];
            byte[] bufOut = new byte[8];
            StringBuffer decoded = new StringBuffer();
            int len = encoded.length();
            int inIndex = 0;
            while (inIndex < len)
            {
                for (int i = 0; i < 8; i++) {
                    bufIn[i] = ((byte)((hexNibbleToInt(encoded.charAt(inIndex++)) << 4) + hexNibbleToInt(encoded.charAt(inIndex++))));
                }
                decoder.decrypt(bufIn, 0, bufOut, 0);
                for (int i = 0; i < 8; i++)
                {
                    char c = (char)bufOut[i];
                    if (c != 0) {
                        decoded.append(c);
                    }
                }
            }
            return decoded.toString();
        }

        public static String convertByteToString(byte[] blk, int off, int len)
        {
            StringBuffer buf = new StringBuffer();
            for (int i = off; i < off + len; i++)
            {
                buf.append("0123456789ABCDEF".charAt(blk[i] >> 4 & 0xF));
                buf.append("0123456789ABCDEF".charAt(blk[i] & 0xF));
            }
            return buf.toString();
        }

        public static String convertByteToString(byte[] blk)
        {
            return convertByteToString(blk, 0, blk.length);
        }

        public static byte[] convertStringToByte(String s)
        {
            byte[] buf = new byte[s.length() / 2];

            int i = 0;
            for (int j = 0; i < s.length(); j++)
            {
                buf[j] = ((byte)("0123456789ABCDEF".indexOf(s.charAt(i)) << 4 & 0xF0)); int
                    tmp43_42 = j; byte[] tmp43_41 = buf;tmp43_41[tmp43_42] = ((byte)(tmp43_41[tmp43_42] | "0123456789ABCDEF".indexOf(s.charAt(i + 1)) & 0xF));i += 2;
            }
            return buf;
        }

        protected static final byte[] cbc_iv = { -2, -36, -70, -104, 118, 84, 50, 16 };

        protected static void BF_cfb64_encrypt(byte[] in, byte[] out, long length, byte[] schedule, byte[] ivec, int num, boolean encrypt)
        {
            int n = num;
            long l = length;
            byte[] tout = new byte[8];



            int i_in = 0;
            int i_out = 0;
            BlowfishCipher decoder = new BlowfishCipher(schedule);

            byte[] iv = ivec;
            if (encrypt) {
                while (l-- > 0L)
                {
                    if (n == 0)
                    {
                        decoder.encrypt(iv, 0, tout, 0);
                        for (int i = 0; i < 8; i++) {
                            iv[i] = tout[i];
                        }
                    }
                    byte c = (byte)(in[(i_in++)] ^ iv[n]);
                    out[(i_out++)] = c;
                    iv[n] = c;
                    n = n + 1 & 0x7;
                }
            }
            int[] tempInts = new int[2];
            while (l-- > 0L)
            {
                if (n == 0)
                {
                    decoder.encrypt(iv, 0, tout, 0);
                    for (int i = 0; i < 8; i++) {
                        iv[i] = tout[i];
                    }
                }
                byte cc = in[(i_in++)];
                byte c = iv[n];
                iv[n] = cc;
                out[(i_out++)] = ((byte)(c ^ cc));
                n = n + 1 & 0x7;
            }
        }

        public static void main(String[] args)
        {
            if (args.length > 0)
            {
                String encrypt = encrypt(args[0]);
                System.out.println("Encrypted:" + encrypt);
            }
        }
    }



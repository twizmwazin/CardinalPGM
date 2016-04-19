package in.twizmwaz.cardinal.tabList;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.event.CycleCompleteEvent;
import in.twizmwaz.cardinal.event.PlayerChangeTeamEvent;
import in.twizmwaz.cardinal.event.PlayerNameUpdateEvent;
import in.twizmwaz.cardinal.event.RankChangeEvent;
import in.twizmwaz.cardinal.event.TeamNameChangeEvent;
import in.twizmwaz.cardinal.module.modules.permissions.PermissionModule;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.rank.Rank;
import in.twizmwaz.cardinal.util.PacketUtils;
import in.twizmwaz.cardinal.util.Strings;
import in.twizmwaz.cardinal.util.Teams;
import net.minecraft.server.DataWatcher;
import net.minecraft.server.DataWatcherRegistry;
import net.minecraft.server.IChatBaseComponent;
import net.minecraft.server.PacketPlayOutEntityDestroy;
import net.minecraft.server.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.PacketPlayOutPlayerInfo;
import net.minecraft.server.PacketPlayOutScoreboardTeam;
import net.minecraft.server.WorldSettings;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSkinPartsChangeEvent;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TabList implements Listener {

    private final String defaultName = "                        ";

    private final Property defaultProperty = new Property("textures", "eyJ0aW1lc3RhbXAiOjE0MTEyNjg3OTI3NjUsInByb2ZpbGVJZCI6IjNmYmVjN2RkMGE1ZjQwYmY5ZDExODg1YTU0NTA3MTEyIiwicHJvZmlsZU5hbWUiOiJsYXN0X3VzZXJuYW1lIiwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzg0N2I1Mjc5OTg0NjUxNTRhZDZjMjM4YTFlM2MyZGQzZTMyOTY1MzUyZTNhNjRmMzZlMTZhOTQwNWFiOCJ9fX0=", "u8sG8tlbmiekrfAdQjy4nXIcCfNdnUZzXSx9BE1X5K27NiUvE1dDNIeBBSPdZzQG1kHGijuokuHPdNi/KXHZkQM7OJ4aCu5JiUoOY28uz3wZhW4D+KG3dH4ei5ww2KwvjcqVL7LFKfr/ONU5Hvi7MIIty1eKpoGDYpWj3WjnbN4ye5Zo88I2ZEkP1wBw2eDDN4P3YEDYTumQndcbXFPuRRTntoGdZq3N5EBKfDZxlw4L3pgkcSLU5rWkd5UH4ZUOHAP/VaJ04mpFLsFXzzdU4xNZ5fthCwxwVBNLtHRWO26k/qcVBzvEXtKGFJmxfLGCzXScET/OjUBak/JEkkRG2m+kpmBMgFRNtjyZgQ1w08U6HHnLTiAiio3JswPlW5v56pGWRHQT5XWSkfnrXDalxtSmPnB5LmacpIImKgL8V9wLnWvBzI7SHjlyQbbgd+kUOkLlu7+717ySDEJwsFJekfuR6N/rpcYgNZYrxDwe4w57uDPlwNL6cJPfNUHV7WEbIU1pMgxsxaXe8WSvV87qLsR7H06xocl2C0JFfe2jZR4Zh3k9xzEnfCeFKBgGb4lrOWBu1eDWYgtKV67M2Y+B3W5pjuAjwAxn0waODtEn/3jKPbc/sxbPvljUCw65X+ok0UUN1eOwXV5l2EGzn05t3Yhwq19/GxARg63ISGE8CKw=");

    private final List<Property> coloredSkins = Arrays.asList(
            /*0, black*/       new Property("textures", "eyJ0aW1lc3RhbXAiOjE0NTI0MTc0NTY5MDEsInByb2ZpbGVJZCI6ImU4MjYwM2RmNDE3ZDRhOTViZDFmMTcyMDY0OGJlMGI0IiwicHJvZmlsZU5hbWUiOiJQYWJsZXRlMTIzNCIsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS85ZjcxNTBkM2U1ZGY3ZjIxNzEyODlmM2M1ODYzYzkyYzJjODkwOGMzMmFkZGM4NmVhN2Y2MWVkODQ3YjY5ZCJ9fX0=", "v4HSztoVOn0TMBHyDymPBItvOpWs8z6twVamfdrE4yhr8HZoBYWzl+qxfWu+8+DNyBSCpN6nr/UQpFErdys9Kk6urIURx8mEeWDXcYOhXrFs7oNpXiD7UvYm4nd+vNr0xbpfQmSXBGIZ+eOei4ThbKdkSIB79mlq0ugbwxCsK2I8kUlUU6+KnsunPr80adcPu9RryAW00Bmta+eP65nIKwUWNKeLb5iHaPq+N/IZ5aKHmLFiSXiWniDB5UAYybkBZFuvosSr4TBpn1pTbEF3PtKpnPM/8mpt+97W1JcCAv0mdFZUr0hT9eMAe3U0r37J4w/RmLd0sCD7zOBX0pIPPIMrXOQ4DfuDbKSJPXyXiLQQrWCHYnOO8+8kiQcoQ427trsb2y+jMwYel2GEU6gS5zOdkkVm8Je6tNxgA8vRGqA8ABW8SVQz7y5spk2CGiTQQbV3EeJwKcZHXAAplkoSB0p8fRE1fEY0+REoETg5TbguZnONm1+PdW/LdLifL2tGClSz6Nb4D513zZuJaFc/dQ7yagJR0cFkuWVuI59ZnoKqiEW+tMsrhn7QwCwGN6eASHd9seNvnTXsGOyZ0iOWFCay7JWOGSMr8iGiYCd92kn4r+UbCJ+OtokHzxFIoEHr2hRnN6thye27/tpP9Is7cmmmrDAKvecGtYfsrCVmTIw="),
            /*1, dark blue*/   new Property("textures", "eyJ0aW1lc3RhbXAiOjE0NTI0MTc1MzgyNDIsInByb2ZpbGVJZCI6ImU4MjYwM2RmNDE3ZDRhOTViZDFmMTcyMDY0OGJlMGI0IiwicHJvZmlsZU5hbWUiOiJQYWJsZXRlMTIzNCIsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8zM2I2NWQ3OGMwYjI5N2RhMzgxYWJlN2JhZmM1ZmE2YzFhZWVhZTY5NDcxZDlhN2JhYWQxZDQ5Y2FmZjVkYjkifX19", "mKqD4+2/uXiIjNmtQDcaGoxghxN3d+YFX5VgVHO1whwWSdEzRtNj3tw5CJOyBZVzQh6GfYqqi38ylfCiBq6t3JAYfaf4BxsDSKyq4yRfNb2JnYQoXGrhIhwMqEEyieXoJUshnBXpneASDf0a+BE5hN29l2vqQGs9VPKggqbt2zpZHyUi6tQMlwOiL+jDKh1bcSr4Wl1Ad8JC/BhUXytOR5u0FWm4vOEsqq8Td7S4Jm+XzgwxogtrpeVhRMFwYL7snjehYF3qh1W9p8PNmJGtZ9AApUctc/qEVnWeCOnDAaKgqozj4ruc4Xk2KMXxj0GmDS6b65C3m71MG+cW/0sS/Rx8vmif9SJum+E8C0X8inNr1nui+3ulmwqC3x0MeVsp81cr+LJIA2dcryKVWV3W6hkYuc3OBUjxrN6NOP495fEsPTFx33eop+pajS8g8Re4drNYHUSy7suzDOq+Yv1XM3hOASN/msCU79/tj1G7HIQ2+RHz2eFZlUWC3IEX+5BM5cf7uK1hhkkbejUkmQSblFGeuTnUdV7Yr6kmn0kXQbHAOIrRvw8d91x1lHoX7yjh+HbG4+RymOcm09MJAfdaut7Q8fINHM7rsiHOT6P0Tj7u1UXUUT9w9YXkVgEPCpjSjsjfkGblq/bzDzg8d/es3aZd00WmqFGjlNMx/EKVf/A="),
            /*2, dark green*/  new Property("textures", "eyJ0aW1lc3RhbXAiOjE0NTI0MTc2MzUyNTgsInByb2ZpbGVJZCI6ImU4MjYwM2RmNDE3ZDRhOTViZDFmMTcyMDY0OGJlMGI0IiwicHJvZmlsZU5hbWUiOiJQYWJsZXRlMTIzNCIsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8xMDU3ZTUxYWE2MmFmMjQ4NjdjNjc1Mzc4NmYyMzRkOWJlYjI1NDEyMjM5MTc4YTcxODZkODE4NjkxNmU2YyJ9fX0=", "P6gT4sSTDPKon9h+Qp/JEK1minlQQRi0ReIsYdYFzHiVpOejPxq4MU+0Ebmvs23mgX2tgqUQV8oeHK9xNCoTB4cKrLMq0U7JJ4C6ShCrQXL3Mr6MilVIDUbP4Fvj9uLU9vFCEiyHVnYwKlnIsnDxnOOBFzki5TyaFTVt2LeNrlso6eh9ARpa5PCAevtEDDrZ51ftvoI268RrbQTfgE7coiDYAcEzgSY9Lm5I9vQxIN6VWhh/tUxHHdn8u1eDIbga/I/EuQZ6H+V0E3THmR55ob4mKZ4e3TYWWRRlOIZPOdFcPIluVdkVZG03cQfw8RYsON/dazBnbME55NTF8U6ovyDWcwYhCEJtRtA4yGMOocX06InRDOkmFk8FcomtJF0WFSSZRbNfxEmG1XXZx1VtKX4eYR97Y/ihOBilN+Aq5yQ2AB+JvEPb1NFiQHdu3mQ/E4tJxKgsorFd75xbPBV7A/mPsrUV2GHvLFJzlBQGDYJCWqqIEdRV/V417j0XlpM+D7UBiIekcsDo2ajOg0v5mQqGF5d1+H16K58PgAOkPLmKwy3lTAnpPWhSWfcrSbkon93eezlD6eMZie8YG2ucaPR9TXYXRwLxK9DrCx5ZfzQV4lgeDSsL2i8kNnYmTLZ1DnsVw8IwWifUFGwwapHFWO1thdy5yifjaa/AOWYTJQE="),
            /*3, dark aqua*/   new Property("textures", "eyJ0aW1lc3RhbXAiOjE0NTI0MTc3MDY0NTAsInByb2ZpbGVJZCI6ImU4MjYwM2RmNDE3ZDRhOTViZDFmMTcyMDY0OGJlMGI0IiwicHJvZmlsZU5hbWUiOiJQYWJsZXRlMTIzNCIsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9mODZiZmEyODRjOGM1ODk3NTFiMzQ1NDA1NTc0NGM4YmM0MGVmNjVlYTZiY2RlYzc3NDg2YzllNDE3N2FmOSJ9fX0=", "LGisiuVbSaI9K2dXN9CsV8SxuXieWvCHTg1mdjUh744AYQBbINiEgEbXiFz3cQXzzJttUAW8MPZyBnl4qxJr5QRXd4mB8bOd6nA9eimRPiCIpS8QG6V5YZWdyEvKuV+xpdWNkB8I4kpgkRFBmYsKNWeg3xhmgeA0QP1kpadGKU4oOMb5vzUzsIVbyycjhtGcRNuGKjZaNoHVssuTUpi83xIqdCPI353rsltncBY4Idheig0cUzBXqvLX0K3pVxVgg+jQxRPRlGdLhiMXXs6IkRj1glvcZO4MEMg4IfA/W6Vq4UiVG6RpubS53jDJDt4G4ZrtSVX07rQOowHlnY8xIgtpSAbW2iy+uHu3mvv4lWATZE7irU3QrRg7PUSZYSIihqY+CWp8eJwhs+JxTTBMqCzfv5HvZg73WVj2003JTIIxtqTyrlBxIa+4DnPBhoCJ+N0v7wmLdZQC+PsVaRAC8nrlhgDewfglW9HHhqU8m5LVa5gDyMlkIOo6w8cW3mOCTqndGNf4GmDVCbli5Yu5SptaZXvibCp8HcNmkJdP6yf4h/hKPEvOc1QzQOm4L5+9jJxGtpZA2+sTDXqucI2TPy5XNxd9+GaZP9J/xMiwihqeGYmNXbjCJdFSE5NX71HH0NbIVQu5G2VIFccuKkz0iDP1vl60ZiHZdVhn3DcL1gY="),
            /*4, dark red*/    new Property("textures", "eyJ0aW1lc3RhbXAiOjE0NTI0MTY3MTU0NzcsInByb2ZpbGVJZCI6ImU4MjYwM2RmNDE3ZDRhOTViZDFmMTcyMDY0OGJlMGI0IiwicHJvZmlsZU5hbWUiOiJQYWJsZXRlMTIzNCIsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS82YWM4ZDlmNjQ1ZDU1MDE4MzRkZGZjNGY4YjVkMzIxNmQ5NGYyMTEyOWE2OWQ4YmE0YzJiZjE4YzE0MzEyOWQifX19", "D7ub7tuNpCdQckdl9tMJMoSF9XfEAhL0UA4hqMNUcm++uHJviVXbI9ePlg98+N5aisqzVj19/Ld1DaJENFH8eKtQ+lhvzS6Rh+x/JF/QGfDGbWSbx9J9H86LvJpY28hQ/XJ1cFSmtqK5xlnXBNilFj+sAtqCJ+KRdPzwnhgvMNqFMmy5op4qjHaQH4QN997DDF8RWt/9dMzoBDS32sr9RAwzyQzuRsa2mHyMbrrPzST78tcyBWk/dddE5zY9qdt7sDtkar3hDOEMgrid+ChxmAlh/LulaXhpW2S0XEsUEY3uBc58iNrnIWaQedaYYaFFdXPe95BPMaMXtgHblRsOhWlvTAXv9P/CB3v0PKlbfH+kFZyf/OgHfkZan575dAdeEl5mVdT6h7rqriau3MIj/MPUX76xMY+0cbNUGG8Dmw5s6Fw2CnkBuRJWaf9NlSynxm0S/K8BqF/UdGCHc3BVYJ54Bhc65KyN2EONaON2OT2p41Ssvt0Z1UEdb9w+0G+pMYJ9qp/firkdsyQC0VQepymxTDvsmhrA+MC/fb4QL47XucJIFGzhi/qbxj+AjKSt6jhbqYUNE74Y4S8U6fsjIko5dEiQ1eVhq+TOsGAiF0M5M84Jpcyh3B/agvJLic4zIwR0AEE91Ok5ZM8hOimhQf206XvYItw3FqNGjdOsiPw="),
            /*5, dark purple*/ new Property("textures", "eyJ0aW1lc3RhbXAiOjE0NTI0MTc5MTAxMDMsInByb2ZpbGVJZCI6ImU4MjYwM2RmNDE3ZDRhOTViZDFmMTcyMDY0OGJlMGI0IiwicHJvZmlsZU5hbWUiOiJQYWJsZXRlMTIzNCIsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS83NmNiYjYzYWFjMzM2OWE2NzI1ZDQyZmEyMjM4OGFhZmUzYzFiM2YyYTE1MTlkMTA3MjE4NDMxOTgzNWNjYyJ9fX0=", "QUWtd7snPe+R9CWdOgAVlagJE79wO4NUGouqctu+/38p2L2b8PRyFydPURaN7gimsBd3+XM6fd2SqS5goc/wj/YXn7FKrIAWP7Wlq1+638006wKaqtyeIXPIOEGBt5fZ/ooYvKDlVjEkyZ1MHwhUVtkmBDUGkvG05WKOJ0LscyCuUw93qfE9LJjowMLubIw1Aq3gSo0dmcN+KcSebHxP7ppThE14BZBrUr2h5zbu1LLYSDKgjFiO3BFkVKFTXtz8P3/kLprQUycT+ojGf8aye2WjO9GlEkiEEj/MLb31B8ImbOZcFWpmqEGfxctKyK2UNZdVrof+Y2qKpZMMVcn6H1SWCM+H/vt7y0wxs9j6kk+xkFTgQJUJY4Y+lBuT+id5Zm0iP3Ua/dCauhlQNezWvCnvx/ICKtGaibzpsouScj/2XMapnQQBNnurTK6v5viDvlt3vF5sdg/pRYDtqyKF6j0prBjQJBayAmANMIefAVYKwGyHONg+JJi72JEm9srIamp1a3ijfP+gYj/1wKtu38w4t3Yfbr7PRO5ArhZbUKvxw+nIDRPwK41SqARU/j9aWVtsud4ASigNno8KNndH9Q/RIfXXukKBNi69PRvPgG+FKhrpN+U+13Zcdzx0mtkMK+ZGb1Qp05Ko7gDfCFawWQE8wKiFC6mIWzWVSeCMqdQ="),
            /*6, gold*/        new Property("textures", "eyJ0aW1lc3RhbXAiOjE0NTI0MTc5NDUxMzEsInByb2ZpbGVJZCI6ImU4MjYwM2RmNDE3ZDRhOTViZDFmMTcyMDY0OGJlMGI0IiwicHJvZmlsZU5hbWUiOiJQYWJsZXRlMTIzNCIsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9jOGM0ZDQ0OGIxZTEzYTI1ZGUzODg0ZGIxN2Q5YjBlZDM4ZGY1ZDEyM2RlNTc3M2YzODIyZWJiNTNhNzYxZTMzIn19fQ==", "gdr3b0Zy/uOlU4VdDuAmnPGhKlg9qImK4zj0qoisJZfiVYrPNPeQCocxVvnkAYqsuTsoe7UUo5/oW/G6Z6AKw+2aapkNbUSwxhdCb2vLmnIt8WGhxTxKcd2OEdCnAmCNgtcF8kF062yK8Enoni2eJI2oV+7MektoFWV5pWBkSmhNMBuw5AraYv9S0+zJTYjX0eANTuNXV+VKnfzMCcKuyOBwqXhNMzL9vmvXTMBJAr9bYx/xH3POO5xhGRrW4NyuWSE9SXhV4NngSR+59kImfZmSA6d9kuK26feZrfRrAME/UV2rjbnT4WWumYzvrroZKJBcq++yBEsVluEagkWzs8UXOtOiNYttp4ETg19aYObXdQSLGFRzTeVCVw4cHVSX6Svbiie/Kyr+s/5fQX7/LCs7uVlclPMrabQiam/DzDRre3hbEHXTfiUCLFQLjyqOQ1+gsPqWN2E/HMj0I9gbKL6qrgRVstvTf97UXqXxXudbOC3EthdgAM4n/lR8s6RqmqwzfdkWAyvYGW2c49tImnEtaltwhzeprURNy/dEbLkU3KYfXx2nVHO7+d67WJscwHiffyxDpLwTWkclIrC7bl2SKyfib1cElDqzXNzKYeqZ595PkiHeBBRjL9CWLTlXcLMWuJtdqvquCXt6oJ7EtalcVhKE6DHXdtBDDYDaWpA="),
            /*7, gray*/        new Property("textures", "eyJ0aW1lc3RhbXAiOjE0NTI0MTc5NjU3ODgsInByb2ZpbGVJZCI6ImU4MjYwM2RmNDE3ZDRhOTViZDFmMTcyMDY0OGJlMGI0IiwicHJvZmlsZU5hbWUiOiJQYWJsZXRlMTIzNCIsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8yYzkzZWJjNzc3YTg3OWVkZWViMTFiOTY2MWNmYTZkNzdkZjk0ZTY1NWM0ZTI4N2Y5NmY0ZmY3OTI1Mjc2In19fQ==", "jL/5arN/cnJbD/DZG9HTo1ckkwZk+XXYUe6QUqbZ0QAm/wecw/mDZqvGwqwnngsgyg0UelxPHvRK3J3z3SdVq7Kavq2UdGkFLr1fe+34bxTnzricHXs4baYDQPOunmbdHgDs+7wwjYJbuQ4nNRE0vQsCmMAe2epchFEDRsiWYuBS0bWCDtQzCBJasGridmH8MIpBmicKQ8OWor9q9T/Pvxh1KEJdpFJuvEGS6xA1J3GxuRT6Iq1elATJNhW3P0MeHU5f/C8N42clLy9K2W8DGm0VuDQbYZmBCaxYcIo3kbkAVVZ6LGHfO8q0kxKIq9q+qXcmn5TQK3BmyjqTGY3PFYRVR9thgA+LWk1s1Ln1jA6ousckJmQJa1eW73mRt91fvRVAMUGm5qrwScL6q2pe3BjBfB3OnilSR3x2cdwM3WkmfN8VuELykJvxHIhDXGCxI5Xg4ghPyZvqW1NNTJmCUokp/fEt+64ZkXoqXzzV88pGZXuVC3ySgg4hdhL4he0tUOVQHttls+lQ2id6R3XaHdh4Hlk/EjSDM5O9auenvKdKAis+vVUV2oqJ+XGb5juD/MOG/INieNfuhQArWeTxyeufsOj8llWEcyYVRVcmJJfjrAagSLuY9OfoNVlSBM2wP92tzQUXQrvHM+mqw9gDlhudOnJAyP6iP8bVsqPN1bU="),
            /*8, dark gray*/   new Property("textures", "eyJ0aW1lc3RhbXAiOjE0NTI0MTgyODg3MzcsInByb2ZpbGVJZCI6ImU4MjYwM2RmNDE3ZDRhOTViZDFmMTcyMDY0OGJlMGI0IiwicHJvZmlsZU5hbWUiOiJQYWJsZXRlMTIzNCIsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9lODQzY2ZjM2ExMmNiNjQ0MWY5NjA5MDIwNjM2NzI2NTdlMDhjNmQ4MzQ0YjUyOGE4NDU5YmQ1MTYyYjJkNDgifX19", "nPEbxncNxOm4I6BEsRQ+nsSy5qXKsvKkKLTYHUkze9fQS5JYyiTgAuN/ouljx8fBSbEBdFWxFZq68WxF5h3QDIX7O9x2OFtx9L7vURndZ9pACBRJehy2Kt50eLlEeNMfg1z12J0ODQ36fKbujUYK4ad2ZkM+IOd+QGRV2EDRqFpKC4NMIiXnQ40RYo5GJBAIuW96kioCvaN7+jbpyKW7ub9RAj7dVlXeBbP7cCYvPHb74Ww/lw/EhcsbrwH92eqrApr/oq0Aa9MoW4FKLsEejXvYFjzn6d44uPddwjkcWnd0B2Z8Z8PU/ajS+ZYQY/RqQKRo72bjjPKmn92A19ULtPTFTSOsxxVmLTSSFbYqujt4hUd7BEcDlFcowfhdXtUQWYp1c0DV63UU+dkp8bmnaSCHoG+goyJJN43eqZwSiln2UzEGb+it/wSE3kgsy8q2X4pNgx67ZVUW1ZdRomsBIO0WoanuYoeCNXKRzwMj2mygocdlSer5ZmsnvqG8e+zt8j4iytYDRZ9AWu4euHPPYPbWozAUUbqzsAkogcqa29Bz0mEbr56Per+4806tmPrg+kheI/pBvqCv+HBMRM5ZEEO9FWkTKl2hvr6t6uTNuXFnlVkwspEJaocE+8JJ0E4WDW2mR997uvYYbohY/HPMUG6cK05SwghM9OhQmD8L1Ao="),
            /*9, blue*/        new Property("textures", "eyJ0aW1lc3RhbXAiOjE0NTI0MTYxNzkxNDYsInByb2ZpbGVJZCI6ImU4MjYwM2RmNDE3ZDRhOTViZDFmMTcyMDY0OGJlMGI0IiwicHJvZmlsZU5hbWUiOiJQYWJsZXRlMTIzNCIsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS80ZjE1NWRiMmIwZjE1YmIxMThjMmNjZjYyMWI5Y2JlNDBiZjQzODk4ZmZkODRhZjczMmMxNWYzZGNlNWM4In19fQ==", "tXZ/NzLISCygr4aGFLVwW6g8lZdoPruN472pVGHepHU+e+spVjOoQQ/vXrIl2nO5EZe4yVsXYDGZK7UD8v3pNfjyXdwAdaRplgQy4B71dvpxoookgN6ytbkr3EV0WucsNCpJPVEAWN8P1DGYVU0lVWyqdphfmbqGaTQOtJonS95TIbfJvmropA86Uc3s6mCOGceb6+wu216IGOtQ1tnBI7wJJF6p3nYm2hXKPDz/ulZNUb6A17T532hfLNRk7tWaPsjzYnKdTFvpVzuVyulfUPz0cxfjQPAreU072/GxKxk2eTlK6qBCT4hOBa+jlnBUUuG8JIL1/HetF5e+svs4LcMzVh1ZSnPsu6ucEpN8VESDK6ErUSuxIm/f2FnaR8eHaqdJBd/1xaA2jAILNP/Y//4G72BdnMnFsv+rVptE+V+yWzg0Pvlv3I0FqGNc+fZWmP4pqjIUd2T7LjtztV7MyBDECg2ASXRhmaaldjKcZAjgaOyPjYkQ8ydFgPMrQB/xR4yiHnivguJWG9ReuwALI480ZKw5VM4evp6JhvYGBkgnrvC+AKGX+gAZhfAnIT+KdUXAJh3LYfy984e1OQQqNCOg+lHR1aPU1RbtSEN878gqYNbMVbZ7Q5fAEo7kz9V9uqmHSrqQ8w4ltUO9W4zbMYJDVVdVAWbzF915ohYpgp8="),
            /*a, green*/       new Property("textures", "eyJ0aW1lc3RhbXAiOjE0NTI0MTgzMjE5NTAsInByb2ZpbGVJZCI6ImU4MjYwM2RmNDE3ZDRhOTViZDFmMTcyMDY0OGJlMGI0IiwicHJvZmlsZU5hbWUiOiJQYWJsZXRlMTIzNCIsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9jMGQzODkzMjYyMWI5NTIzZTlkODdjODcxMzEyNTk3ZTU0Y2QyMjRjODQ0NmY2NTE0ODY1ODE0Y2RlMzhhMTIifX19", "huZ5VsNJVXyj1jijC2KE3rE/XbF47jfj/dApSKRqMbTmHQhi3AEFUAetgN0TsBJWaynz/bgADVTY84WsDeeZyW6u1FZHQtOyvq6zxVCw2L1tFrjO7Ts0AYXpNvaZawz+r9OuM01Y62z9oK4VwA7e9oFHDSuo4mExcTgd35cqyxJmqNA2k5xMh88BBiKumJNTenzTEqfQaaWesSmRJWnxZ09zZKhZb2E0m4ekymZPKZWPuxxfOenaFWlpyltLnx/2pC7VRkG1v+zoBe/VmfsEtu5qWPPS8UPtg1Fpx+Q3GtxIApGj0Ni/DiKkaOmOY/5HH9uYD4BtJ3NjXEFCWbkQOXEgLrgwdstRVL53opC3+07QZTbnXVNA2Ua76Gu6T8j+KMxpA0+q0nS+FQZCL2TEt2Pm9nsAZx43kvQ/iA3hnM1hZ64jSQ4nj38mUJ7bnmqM2bcZpQMtIDzwMwswMLh9/jpYFZBK9p5tG2TW91RjSne6R0sOyzZyfOBX/T8oNohBCSVokD4+8SGyBGzknUb1VE0YOZ5HOj3N7agGxhWyPB6DrYCUT8hljtezFO+iPBSBLVo2yuX1PMrrQYB9ir+rTc7mjOYWmL6ENrkSN52fBDCd4yLhPZyU0AP4ov+Nl175c32e5f8Ihhz7IRnVhNDzOE12WS55ynn1IofblMSgfPU="),
            /*b, aqua*/        new Property("textures", "eyJ0aW1lc3RhbXAiOjE0NTI0MTgzNDc3NjIsInByb2ZpbGVJZCI6ImU4MjYwM2RmNDE3ZDRhOTViZDFmMTcyMDY0OGJlMGI0IiwicHJvZmlsZU5hbWUiOiJQYWJsZXRlMTIzNCIsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS83YWQzYTk4NmU4N2E3ZDZjMzk5MTNjNmM5MmE1MmNkNTI0MmUyNmU2ZDc4NzRhYzVkYmFiMDdkODNiYzE4NCJ9fX0=", "QwTBMrsbG4EGg5GnaYGyWy5S6G7UWJTTafe/22OMDEsmIy2mcaPzTRHdxQjeWPa8Zk5wGlp7ehZMbC262DRdDG5Wg7SKuAkxNdow/u8BhrUJg4Z9nQhfOauoN2tk/F1j+isV+xVi58EsEhhsr9h/1s9vxJKI2GBJJUmTjZ8xzYmcsihpoM4LaHKRu8sXWQ1MHa5QBgqzOy8UPRabFFNg/5GT8xo0UqM/5FlsYMdiIPsEdOi5bMPsU6ZTco/hWpqTMAlhDMw3hLUxvknnQ6Pf1Mi6GOqTSucaKggAyrj++p9LMtihwBOvRHhWUfIPTKdZ05JllS8Q6aZioOqVbsd+GLHYZp/PKtwKNC62rPzpqZMIBw3HgZ5ciQhSmDMr/l2dDUKEn/LIAJew9P+GXVEpPTAJSLy2IkcEKzrtU5ZSTmbZwzzldviK0+tPVCE3AzMcnYqoVbj4uOpo9Uo/uaunLWm8/PWcjRw3qy8c3czxC1xVynEDA5J6wjo8oqjSnEOuHuJ6cFLCYUf4QMlRmVIaA4lllAOOr+68QEHzek+DRc6rNgkUoqCcHF53YCfH7JItpSxxTZ6snFS2LI20KFUp93DibVEkxifkyQLbqB1OAcyDXkji8HJ7IKUMAUKoNFpLL5eANJsv/96P81N16rGA3THLe5iz17L5xVX/hpruKAo="),
            /*c, red*/         new Property("textures", "eyJ0aW1lc3RhbXAiOjE0NTI0MTgzNjk3OTUsInByb2ZpbGVJZCI6ImU4MjYwM2RmNDE3ZDRhOTViZDFmMTcyMDY0OGJlMGI0IiwicHJvZmlsZU5hbWUiOiJQYWJsZXRlMTIzNCIsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS84OGNiOTNlOTgwZjYyOTMxMzYwNGNkMzMzODEzOTRjNTA5ZjljMTIxOGRjMjYyM2Q2NTBlZmI4ZjNkMjAifX19", "FqA2ErdpdSPLYrb+O0O1i5dxEr9ZD7ZhaeiSZmU8QSf6+yA8eGo+KZo0N7EHC6YFXnxkgySFqJYk7itcHQ5bLRKpKCaSKAXP4XX5a94/7x6lNa8ev82L+Er3A/mH/0cllxmngqLY5vK8Hej09NoMMNNAhtp+f07TkqLPCi3J6G4mFK4zHAId1gEIEyj7JxlTDeOeU0MRUE4bbngyt2h5VsGpitFaYWJZTSnl8XsXr+6/diXk1nM9smKmmwIv8C02Ufdw3N4/fB93qWfuDUmpd4RLck6XJpp8qJ95twQTmdlpEkUtTt258mHnCV/pjMY9T0J7R1MDbYajkfhDQ1xIdKpVZEsYroEVv4jNOedbRHccF5XZLG7QcYGUk22A53XT0/zuyIBfQbYk/XShABdIHoNW+PNscrtahAmqqGmxkxQqZdI7DdB6V4f5J2YpLkt6k/nXvV1uA+Gm2uPylPvvJusVJJD0Z+BroyM4SsXFfaN9oAqGmNFMdIWR+17vl9TZmfN+K3KodMeiSTcVNUFKVAtMFXSaz80UzmKAB1D+EsZQrJ/Qzo4+OP52TwMmlLOBUY3TvgZkJrK+2MC5kGnwJlW3atjR8JlsKTdNJx/xoCg8HRbaqdxQAfz7zYBRnYfFeuf9Uvb0G7RW33CO0ifqP0gi9HUoQH3LxAiJ0zNqh+o="),
            /*d, light purple*/new Property("textures", "eyJ0aW1lc3RhbXAiOjE0NTI0MTgzODM4OTUsInByb2ZpbGVJZCI6ImU4MjYwM2RmNDE3ZDRhOTViZDFmMTcyMDY0OGJlMGI0IiwicHJvZmlsZU5hbWUiOiJQYWJsZXRlMTIzNCIsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9lN2E3ZmY2M2RjZDU4NWM0YzZmNjM4OWI0ODZlMzBkNjg0MjE2NGZjNDRlOTQ2N2I4OWM0ZTFjZTZjMzg2OWY5In19fQ==", "HMmoTUZECnTE/LoW4y4+yH7YENNOHqFdhYPFmULfFsEYjU/cY0eUu2LmlFcfjox0RTHqGebR0OmGnfVgim/p6n1RQtOXW0dt3GZKgrU10LPGRvEwQ6SMAB9/o5qKL+tnNDAtzbgrUJQh6kQGI7EU89h1KWeUpXmksPhnAPTvuXhFYEj467Ocg5Jz+CT1n8x4Gu0lXI8v8jHW+G/KP2JSQkLeLkqVRb8dGs9laFRvVwXQ3zLgTFgksgwGnuSlWTxoKVS//oLjwlZtiSowg1g+mQJJZxoH8esfAFGsxNKV+7JrdN13PfCkbdZYDpm5eom4SdrSflP3DXCBuGvBSFoStDYYegZEY5t7GhRizubZOU8ze3hfnbU8HqXZH4wqvmHzH7PhM6edQUrgJ+j9HV5jfpV9fQ9vhtlAFg9x1s9V8+yxpQ827FIqwyR4LY5cVZHyQCl1vkeCSuImrIUmYZ3ZsbQXzODDlzLEox++YDiEnUeuRCdJi4MInO9oKbZnZltEO4fsyWj7dRgQZQKWCR+iXaWiGsNmiiJiishn2+dEjFGg0L0pG6KE4XmkCJtDX2VvvgK3q4e9a0mZucZn68SjV3CcBghXkZ31pKzxxaQrKQoxs1KZFfG2UL7QDGmUURZxwWYzOo6KhI9eyAx41mujy8xlzarQzUErN4cwoFoPSmA="),
            /*e, yellow*/      new Property("textures", "eyJ0aW1lc3RhbXAiOjE0NTI0MTg0MDE0NzAsInByb2ZpbGVJZCI6ImU4MjYwM2RmNDE3ZDRhOTViZDFmMTcyMDY0OGJlMGI0IiwicHJvZmlsZU5hbWUiOiJQYWJsZXRlMTIzNCIsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8zYzY2ZDFmMDhkZmRmZTY2ZjljZDExYjA1MmY0YzM1YTdkMjk0MWZmMjNhOTRjY2UyM2Y3ZjgyMTg1NzcyZDgifX19", "Rs2GuNrHjFBLk09ymZ/3UpTNSbbjmnZ3WvzA8n0X0gwzNHAx+8u/pNWO2uMEW1TZE5fhmrRAb8krfEpr/D5RdeXl+Se7NRch4mxpWqz4jiipMigFgGnf/s0JY+/dn5amGnoHKzqsktHFx3qwwOjGaz2jj1vhysuzQLGptnurn9wnRgVIueWfR2Cctc0v1pJ9jBVx/gkG3N+2Wznml+50pphxhcYBtfUKtwnMRxHIOz0me1KuRhqmBtmMzoQUArJXiz7cAX8cRlTqUg2ilY4UYLxNsH3cyaJi//tOzpk7EEwo2W1vYT/ZqiHTUvDBeRSu4Or9YZ7TwF/klbSnZJqaC2X0du00QcaoGAvFPY+A9HXZ3QII7k4g0M+aI3huiDUQX24O3p9h4J4dKJpktq1FH0G271uf5m3DMQlAQHTJWP07r3rL/23hAALMtIjE4SvUANd+WBxAeAlgSQamWk2YKQv/TxnNlr6tZMKOz/L1xsXJdn4eATWO786wH5IlxHKwgIQnmEYPSZX3AYsGtIsYuQhttbjmqYiecNdywXy6/WpZwhUHWW2aKuvkczJ8kX5Mcq+viVQRWRJNsCwqwzeXMLEX5tpNGOlQAyRJ/lyGUYJKmEXyqWXIxJstbJG09OR/G9V53ZhNa1hEaaVGv1FwZ9WdH1xHhGyJySz9JtrmKQ0="),
            /*f, white*/       new Property("textures", "eyJ0aW1lc3RhbXAiOjE0NTI0MTg0MjUyNTQsInByb2ZpbGVJZCI6ImU4MjYwM2RmNDE3ZDRhOTViZDFmMTcyMDY0OGJlMGI0IiwicHJvZmlsZU5hbWUiOiJQYWJsZXRlMTIzNCIsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9jMWViNWQ1MzExZjI1YjQ5MmEyMTIyYWM0YTNkYWUzZjNiMmRmNTc1NmFjYWM4ZjVjYjk2NWEyYjhjMGY4In19fQ==", "LHeNKZWS3u7hmMmxqjEiCLAlhHRR4ZzQDZkCwJNid0odX38Qm98teyCwg4VmYKUrovIpqX3xReYiZ2LIY2SQDz6nARj6qarHQHDMgZ6Itqfd8jue5ZlzbwRwv9Fmirxdq67yA/VAMmy7Hel60X39PF/qVlVmA1k9nFz2NDmMlASA61nI2oEjfvdwRgODAG0rSkocIqxpZ8y/hUAsUsP2NPISIRl+yY+QLpzkx56+iTvLvYsYbhFMMJyjshjgL6j/TH9XRyjfqxfthTiKrH7zYSbxIb1nQC+Osrzg2EN9M2BPfyvF/MiFsQGu8It9CXSSR6ZFqTnDmhteFySiOrC8WF6F6rZL+vMYLSgke4vixLFLLdgdb1NBBMy8wGfEJFfLGs0n7UcnHERLg8ZTzz3yov6vKVd5dqv8uClQGHbv4iHVpvvepZ8BUuPH4PQgxqhs1akQ/q9B0RVXucigEcQMWfBAftGEDUI9PL17jjjsNbLYnX7yjSV3AWi3PPFVs7JWXrG+9KQPYHO1OuoA0ld3gA50+nSRXqcpDrvxRqo88MlqAv54Wc/I/lYOfpzx9BCgQsMz7n6wq22BsGLhNdbB+Usw/GB8s50KDZ91Zigc2REljgyoabzNBMHa/ACaPiBuFZ8ApBd84no+ipnpVJXnNcFxSH44AShuIcZaCdBlwbw=")
    );

    private int columnsPerTeam = 0;

    private HashMap<UUID, GameProfile> fakePlayer = new HashMap<>();
    private HashMap<String, GameProfile> teamTitles = new HashMap<>();
    private List<GameProfile> emptyPlayers = new ArrayList<>();
    private HashMap<GameProfile, Integer> entityIDs = new HashMap<>();

    private HashMap<Player, List<GameProfile>> playerView = new HashMap<>();

    public TabList() {
        entityIDs.put(null, 100);
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(Cardinal.getInstance(), new Runnable() {
            @Override
            public void run() {
                for (Player view : Bukkit.getOnlinePlayers()) {
                    broadcastTabListPacket(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_LATENCY, getPlayer(view), view.getPlayerListName(), getPlayerPing(view));
                }
            }
        }, 0L, 600L);
        scheduler.scheduleSyncRepeatingTask(Cardinal.getInstance(), new Runnable() {
            @Override
            public void run() {
                for (Player viewer : Bukkit.getOnlinePlayers()) {
                    for (TeamModule team : Teams.getTeams()) {
                        if (team.isObserver()) continue;
                        List<DataWatcher.Item<?>> items = Lists.newArrayList();
                        items.add(new DataWatcher.Item<>(DataWatcherRegistry.a.a(12), (byte) 127));
                        createPlayerWithParts(items, viewer, getTeam(team));
                    }
                }
            }
        }, 0L, 20L);
        scheduler.scheduleSyncRepeatingTask(Cardinal.getInstance(), new Runnable() {
            @Override
            public void run() {
                for (Player viewer : Bukkit.getOnlinePlayers()) {
                    for (TeamModule team : Teams.getTeams()) {
                        if (team.isObserver()) continue;
                        List<DataWatcher.Item<?>> items = Lists.newArrayList();
                        items.add(new DataWatcher.Item<>(DataWatcherRegistry.a.a(12), (byte) 0));
                        createPlayerWithParts(items, viewer, getTeam(team));
                    }
                }
            }
        }, 5L, 20L);
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void beforeCycleComplete(CycleCompleteEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            broadcastTeamPacket(player.getName(), 80, 3);
        }
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onCycleComplete(CycleCompleteEvent event) {
        this.columnsPerTeam = 4 / (Teams.getTeams().size() - 1);
        if (columnsPerTeam == 0) columnsPerTeam = 1;
        resetTeams();
        updateAll(null);
        for (Player player : Bukkit.getOnlinePlayers()) {
            sendPlayersParts(player);
        }
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        updateAll(event.getPlayer());
        sendPlayersParts(event.getPlayer());
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onPlayerLeave(PlayerQuitEvent event) {
        playerView.remove(event.getPlayer());
        removePlayer(event.getPlayer());
        renderAllTeamTitles();
        updateAll(null);
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onTeamChange(PlayerChangeTeamEvent event) {
        renderAllTeamTitles();
        updateAll(event.getPlayer());
    }

    @EventHandler
    public void onTeamChangeName(TeamNameChangeEvent event) {
        renderTeamTitle(event.getTeam());
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onDisplayNameChange(PlayerNameUpdateEvent event) {
        if (!playerView.containsKey(event.getPlayer())) return;
        broadcastTabListPacket(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_DISPLAY_NAME, getPlayer(event.getPlayer()), event.getPlayer().getPlayerListName(), 0);
    }

    @EventHandler
    public void onRankChange(RankChangeEvent event) {
        if (!event.isOnline()) return;
        updateAll(null);
    }

    @EventHandler
    public void onPlayerChangeSkinParts(PlayerSkinPartsChangeEvent event) {
        renderPlayerParts(event.getPlayer());
    }

    private void setupPlayer(Player player) {
        playerView.put(player, new ArrayList<GameProfile>());
        for (int i = 0; i < 81; i++) {
            if (i == 80) {
                sendTeamPacket(player, null, i, 0);
                continue;
            }
            GameProfile fakePlayer = getFakePlayer(player);
            sendTeamPacket(player, fakePlayer.getName(), i, 0);
            playerView.get(player).add(fakePlayer);
        }
        PacketPlayOutPlayerInfo listPacket = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER);
        for (Map.Entry<UUID, GameProfile> entry : fakePlayer.entrySet()) {
            listPacket.add(getPlayerInfo(listPacket, entry.getValue(), Bukkit.getPlayer(entry.getKey()).getPlayerListName(), getPlayerPing(Bukkit.getPlayer(entry.getKey()))));
            sendTeamPacket(player, entry.getValue().getName(), 80, 3);
        }
        for (Map.Entry<String, GameProfile> entry : teamTitles.entrySet()) {
            listPacket.add(getPlayerInfo(listPacket, entry.getValue(), getTeamTitle(Teams.getTeamById(entry.getKey()).get()), 1000));
        }
        for (GameProfile emptyPlayer : emptyPlayers) {
            listPacket.add(getPlayerInfo(listPacket, emptyPlayer, defaultName, 1000));
        }
        PacketUtils.sendPacket(player, listPacket);
    }

    private void updateAll(Player prioritized) {
        if (prioritized != null) update(prioritized);
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (prioritized != null && player.equals(prioritized)) continue;
            update(player);
        }
    }

    private void update(Player player) {
        if (!playerView.containsKey(player)) setupPlayer(player);
        TeamModule prioritized = Teams.getTeamByPlayer(player).orNull();
        int col = prioritized != null && prioritized.isObserver() ? 0 : columnsPerTeam;

        int biggestTeamCol = 18 - obsRows();

        renderObs(player, 20 - obsRows());

        if (prioritized != null && !prioritized.isObserver()) renderTeam(player, prioritized, 0, biggestTeamCol);
        for (TeamModule team : Teams.getTeams()) {
            if (!team.isObserver() && !team.equals(prioritized)) {
                if (col > 3) {
                    updateTabListSlot(getTeam(team), player, 80, 0);
                } else {
                    renderTeam(player, team, col, biggestTeamCol);
                    col = col + columnsPerTeam;
                }
            }
        }
    }

    public void renderAllTeamTitles() {
        for (TeamModule team : Teams.getTeams()) {
            renderTeamTitle(team);
        }
    }

    public void renderTeamTitle(TeamModule team) {
        if (team.isObserver()) return;
        broadcastTabListPacket(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_DISPLAY_NAME, getTeam(team), getTeamTitle(team), 0);
    }

    private void renderTeam(Player player, TeamModule team, int col, int maxRows) {
        updateTabListSlot(getTeam(team), player, 0, col);
        int row = team.contains(player) ? 2 : 1;
        int colOffset = 0;
        if (team.contains(player)) updateTabListSlot(getPlayer(player), player, 1, col);
        for (Player render : getSortedPlayerList(team)) {
            if (render.equals(player)) continue;
            if (colOffset > columnsPerTeam) {
                updateTabListSlot(getPlayer(render), player, 80, 0);
            } else {
                if (render.equals(player)) continue;
                updateTabListSlot(getPlayer(render), player, row, col + colOffset);
                row++;
                if (row > maxRows) {
                    row = 1;
                    colOffset++;
                }
            }
        }
    }

    private void renderObs(Player player, int row) {
        TeamModule team = Teams.getTeamById("observers").get();
        int col = team.contains(player) ? 1 : 0;
        if (team.contains(player)) updateTabListSlot(getPlayer(player), player, row > 19 ? 80 : row, 0);
        for (Player render : getSortedPlayerList(team)) {
            if (render.equals(player)) continue;
            if (row > 19) {
                updateTabListSlot(getPlayer(render), player, 80, 0);
            } else {
                if (render.equals(player)) continue;
                updateTabListSlot(getPlayer(render), player, row, col);
                col++;
                if (col > 3) {
                    col = 0;
                    row++;
                }
            }
        }
    }

    private int getBiggestTeamSize() {
        int biggestTeam = 0;
        for (TeamModule team : Teams.getTeams()) {
            if (!team.isObserver() && team.size() > biggestTeam) biggestTeam = team.size();
        }
        return biggestTeam;
    }

    private int obsRows() {
        int biggestTeamCol = (getBiggestTeamSize() + (columnsPerTeam - 1))/ columnsPerTeam;
        int maxObsRows = 18 - biggestTeamCol;
        int obsRows = (Teams.getTeamById("observers").get().size() + 3 )/ 4;
        if (obsRows > maxObsRows) obsRows = maxObsRows;
        return obsRows == 0 ? -1 : obsRows;
    }

    private String getTeamTitle(TeamModule team) {
        return team.size() + "" + ChatColor.DARK_GRAY + "/" + ChatColor.GRAY + team.getMax() + " " + team.getColor() + ChatColor.BOLD + team.getName();
    }

    private GameProfile getPlayer(Player player) {
        if (!fakePlayer.containsKey(player.getUniqueId())) {
            GameProfile profile = getProfile(getPlayerSkin(player));
            broadcastTabListPacket(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, profile, player.getPlayerListName(), getPlayerPing(player));
            broadcastTeamPacket(profile.getName(), 80, 3);
            fakePlayer.put(player.getUniqueId(), profile);
        }
        return fakePlayer.get(player.getUniqueId());
    }

    private void removePlayer(Player player) {
        if (!fakePlayer.containsKey(player.getUniqueId())) return;
        GameProfile profile = getPlayer(player);
        broadcastUpdateTabListSlot(profile, 80, 0);
        broadcastTabListPacket(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, profile, "", 0);
        deletePlayerWithParts(profile);
        fakePlayer.remove(player.getUniqueId());
        entityIDs.remove(profile);
    }

    private GameProfile getTeam(TeamModule team) {
        if (!teamTitles.containsKey(team.getId())) {
            GameProfile profile = getProfile(coloredSkins.size() > team.getColor().ordinal() ? coloredSkins.get(team.getColor().ordinal()) : defaultProperty);
            broadcastTabListPacket(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, profile, getTeamTitle(team), 1000);
            broadcastTabListPacket(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_DISPLAY_NAME, profile, getTeamTitle(team), 1000);
            broadcastTeamPacket(profile.getName(), 80, 3);
            teamTitles.put(team.getId(), profile);
        }
        return teamTitles.get(team.getId());
    }

    private void resetTeams() {
        if (teamTitles.isEmpty()) return;
        for (GameProfile profile : teamTitles.values()) {
            broadcastUpdateTabListSlot(profile, 80, 0);
            broadcastTabListPacket(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, profile, "", 0);
        }
        teamTitles.clear();
    }

    private GameProfile getFakePlayer(Player viewer) {
        if (playerView.get(viewer).containsAll(emptyPlayers)) {
            GameProfile profile = getProfile(defaultProperty);
            broadcastTabListPacket(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, profile, defaultName, 1000);
            broadcastTabListPacket(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_DISPLAY_NAME, profile, defaultName, 1000);
            broadcastTeamPacket(profile.getName(), 80, 3);
            emptyPlayers.add(profile);
            return profile;
        } else {
            for (GameProfile emptyPlayer : emptyPlayers) {
                if (playerView.get(viewer).contains(emptyPlayer)) continue;
                return emptyPlayer;
            }
            return emptyPlayers.get(0);
        }
    }

    private GameProfile getProfile(Property texture) {
        GameProfile game = new GameProfile(UUID.randomUUID(), Strings.trimTo(UUID.randomUUID().toString(),0,8));
        if (Bukkit.getOnlineMode()) game.getProperties().put("textures", texture);
        return game;
    }

    private void broadcastUpdateTabListSlot(GameProfile player, int row, int col) {
        for (Player viewer : Bukkit.getOnlinePlayers()) {
            updateTabListSlot(player, viewer, row, col);
        }
    }

    private void updateTabListSlot(GameProfile player, Player viewer, int row, int col) {
        if (!playerView.containsKey(viewer)) setupPlayer(viewer);

        int i = rowAndCol(row, col);
        if (playerView.get(viewer).contains(player) && playerView.get(viewer).indexOf(player) == i) return;

        if (playerView.get(viewer).contains(player)) {
            GameProfile fakePlayer = getFakePlayer(viewer);
            sendTeamPacket(viewer, fakePlayer.getName(), playerView.get(viewer).indexOf(player), 3);
            playerView.get(viewer).set(playerView.get(viewer).indexOf(player), fakePlayer);
        }

        if (i > 79) {
            sendTeamPacket(viewer, player.getName(), 80, 3);
        } else {
            sendTeamPacket(viewer, playerView.get(viewer).get(i).getName(), 80, 3);
            sendTeamPacket(viewer, player.getName(), i, 3);
            playerView.get(viewer).set(i, player);
        }

    }

    private void broadcastTabListPacket(PacketPlayOutPlayerInfo.EnumPlayerInfoAction action, GameProfile game, String displayName, int ping) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            sendTabListPacket(player, action, game, displayName, ping);
        }
    }

    private void sendTabListPacket(Player player, PacketPlayOutPlayerInfo.EnumPlayerInfoAction action, GameProfile game, String displayName, int ping) {
        PacketPlayOutPlayerInfo listPacket = new PacketPlayOutPlayerInfo(action);
        if (displayName.equals(player.getPlayerListName())) displayName = displayName.replace(player.getName(), ChatColor.BOLD + player.getName());
        listPacket.add(getPlayerInfo(listPacket, game, displayName, ping));
        PacketUtils.sendPacket(player, listPacket);
    }

    private PacketPlayOutPlayerInfo.PlayerInfoData getPlayerInfo(PacketPlayOutPlayerInfo listPacket, GameProfile game, String displayName, int ping) {
        return listPacket.new PlayerInfoData(game, ping < 0 ? 1000 : ping, WorldSettings.EnumGamemode.SURVIVAL, IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + StringEscapeUtils.escapeJava(displayName) + "\"}"));
    }

    private void broadcastTeamPacket(String fakePlayer, int slot, int action) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            sendTeamPacket(player, fakePlayer, slot, action);
        }
    }

    private void sendTeamPacket(Player player, String fakePlayer, int slot, int action) {
        String team = "\000TabView" + (slot < 10 ? "0" + slot : slot);
        Collection<String> players = fakePlayer == null ? Collections.<String>emptyList() : Collections.singletonList(fakePlayer);
        PacketPlayOutScoreboardTeam teamPacket = new PacketPlayOutScoreboardTeam(action, team, team, "", "", -1, "never", "never", 0, players);
        PacketUtils.sendPacket(player, teamPacket);
    }

    private Property getPlayerSkin(Player player) {
        return getPlayerSkin(((CraftPlayer) player).getProfile());
    }

    private Property getPlayerSkin(GameProfile profile) {
        for(Property property : profile.getProperties().get("textures")) {
            return new Property("textures", property.getValue(), property.getSignature());
        }
        return defaultProperty;
    }

    private int getPlayerPing(Player player) {
        return ((CraftPlayer) player).getHandle().ping;
    }

    private int rowAndCol(int row, int col) {
        return  row + col * 20;
    }

    private void sendPlayersParts(final Player viewer) {
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Cardinal.getInstance(), new Runnable() {
            @Override
            public void run() {
                for (Player render : Bukkit.getOnlinePlayers()) {
                    createPlayerWithParts(render, viewer, getPlayer(render));
                }
            }
        },1L);
    }

    private void renderPlayerParts(Player render) {
        for (Player viewer : Bukkit.getOnlinePlayers()) {
            createPlayerWithParts(render, viewer, getPlayer(render));
        }
    }

    private void createPlayerWithParts(Player realPlayer, Player viewer, GameProfile profile) {
        createPlayerWithParts(((CraftPlayer)realPlayer).getHandle().getDataWatcher().c(), viewer, profile);
    }

    private void createPlayerWithParts(List<DataWatcher.Item<?>> data, Player viewer, GameProfile profile) {
        if (!entityIDs.containsKey(profile)) entityIDs.put(profile, Collections.max(entityIDs.values()) + 1);
        int id = Integer.MAX_VALUE - entityIDs.get(profile);
        UUID uuid = profile.getId();
        PacketPlayOutNamedEntitySpawn spawnPacket = new PacketPlayOutNamedEntitySpawn(id, uuid, 0, -1000, 0, (byte)0, (byte)0, data);
        PacketUtils.sendPacket(viewer, spawnPacket);
    }

    private void deletePlayerWithParts(GameProfile profile) {
        if (!entityIDs.containsKey(profile)) return;
        PacketUtils.broadcastPacket(new PacketPlayOutEntityDestroy(Integer.MAX_VALUE - entityIDs.get(profile)));
    }

    private List<Player> getSortedPlayerList(TeamModule team) {
        List<Player> players = (List<Player>) team;
        Collections.sort(players, new Comparator<Player>() {
            @Override
            public int compare(Player player1, Player player2) {
                UUID uuid1 = player1.getUniqueId();
                UUID uuid2 = player2.getUniqueId();
                if (player1.isOp() && !player2.isOp()) return -1;
                if (!player1.isOp() && player2.isOp()) return 1;
                if (PermissionModule.isDeveloper(uuid1) && !PermissionModule.isDeveloper(uuid2)) return -1;
                if (!PermissionModule.isDeveloper(uuid1) && PermissionModule.isDeveloper(uuid2)) return 1;
                for (Rank rank : Rank.getRanks()) {
                    if (!rank.isStaffRank()) continue;
                    if (rank.contains(uuid1) && !rank.contains(uuid2)) return -1;
                    if (!rank.contains(uuid1) && rank.contains(uuid2)) return 1;
                }
                if (Rank.isMapAuthor(uuid1) && !Rank.isMapAuthor(uuid2)) return -1;
                if (!Rank.isMapAuthor(uuid1) && Rank.isMapAuthor(uuid2)) return 1;
                for (Rank rank : Rank.getRanks()) {
                    if (rank.isStaffRank()) continue;
                    if (rank.contains(uuid1) && !rank.contains(uuid2)) return -1;
                    if (!rank.contains(uuid1) && rank.contains(uuid2)) return 1;
                }
                return player1.getName().compareTo(player2.getName());
            }
        });
        return players;
    }
}

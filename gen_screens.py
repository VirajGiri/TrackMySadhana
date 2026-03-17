from PIL import Image,ImageDraw,ImageFont
W,H=1080,1920
BG=(26,10,0);CARD=(45,26,10);PRI=(141,75,26)
GOLD=(255,193,7);TS=(224,192,144);WHT=(255,255,255)
TRK=(74,42,16);RED=(255,82,82);ORG=(255,152,0)
def fnt(sz,s="r"):
 p={"b":"C:/Windows/Fonts/arialbd.ttf","r":"C:/Windows/Fonts/arial.ttf","i":"C:/Windows/Fonts/ariali.ttf"}
 try: return ImageFont.truetype(p.get(s,p["r"]),sz)
 except: return ImageFont.load_default()
def base(): return Image.new("RGBA",(W,H),(*BG,255))
def sb(d):
 d.rectangle([0,0,W,72],fill=(*PRI,255))
 d.text((60,14),"9:41",font=fnt(36,"b"),fill=WHT)
 d.rectangle([W-130,18,W-58,54],outline=WHT,width=3)
 d.rectangle([W-125,24,W-84,48],fill=WHT)
 d.rectangle([W-58,30,W-52,42],fill=WHT)
 for i in range(4): d.rectangle([W-220+i*24,54-6-i*5,W-204+i*24,54],fill=(*WHT,150+i*25))
def tb(d,title,back=False,y=72):
 d.rectangle([0,y,W,y+168],fill=(*PRI,255))
 d.text((150 if back else 48,y+57),title,font=fnt(54,"b"),fill=GOLD)
 if back:
  d.line([(90,y+57),(54,y+84)],fill=GOLD,width=8)
  d.line([(54,y+84),(90,y+111)],fill=GOLD,width=8)
def bn(d,act="d"):
 y=H-168
 d.rectangle([0,y,W,H],fill=(*PRI,255))
 for tid,lbl,cx in [("d","Dashboard",180),("h","History",540),("r","Reports",900)]:
  c=GOLD if tid==act else TS
  d.ellipse([cx-30,y+16,cx+30,y+76],fill=(*c,200))
  d.text((cx-len(lbl)*11,y+84),lbl,font=fnt(32),fill=c)
def fab(d):
 x,y=W-150,H-168-96
 d.ellipse([x-80,y-80,x+80,y+80],fill=(*GOLD,255))
 d.line([(x-40,y),(x+40,y)],fill=(*BG,255),width=14)
 d.line([(x,y-40),(x,y+40)],fill=(*BG,255),width=14)
def rr(d,x,y,w,h,r=40,fill=None,ol=None,olw=4):
 f=fill or CARD
 kw={}
 if ol: kw["outline"]=(*ol,255); kw["width"]=olw
 d.rounded_rectangle([x,y,x+w,y+h],radius=r,fill=(*f,255),**kw)
def pb(d,x,y,w,h,pct):
 d.rounded_rectangle([x,y,x+w,y+h],radius=h//2,fill=(*TRK,255))
 fw=max(h,int(w*pct/100))
 d.rounded_rectangle([x,y,x+fw,y+h],radius=h//2,fill=(*GOLD,255))
def sv(img,n):
 path="D:/S T A R K/Viraj/GITHUB/TrackMySadhana/"+n
 img.convert("RGBA").save(path,"PNG",optimize=True)
 print("Saved",n)

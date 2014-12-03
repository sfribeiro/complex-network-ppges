dadosGA<-read.csv("C:\\Users\\Jamisson\\workspace\\complexnetwork\\Results\\MyGAoriginal_Full_m0.1_1000_Nd100_Griewank_Dim50_100000.csv",sep=";")  
dadosProposta<-read.csv("C:\\Users\\Jamisson\\workspace\\complexnetwork\\Results\\MyGA_Full_m0.1_1000_Nd100_Griewank_Dim50_100000.csv",sep=";")

seriesGA<-ts(dadosGA,names=c("Fitness","Average Fitness"))
seriesProposta<-ts(dadosProposta,names=c("Fitness","Average Fitness"))

FitGA<-c(dadosGA[,1])
FitProposta<-c(dadosProposta[,1])
boxplot(FitGA,FitProposta, names=c("GA","Proposta"),main="Fitness")

AFga<-c(dadosGA[,2])
AFproposta<-c(dadosProposta[,2])
boxplot(AFga,AFproposta, names=c("GA","Proposta"),main="Average Fitness")


//test migration
dadosFull4<-read.csv("C:\\Users\\Jamisson\\workspace\\complexnetwork\\Results\\MyGA_Full_m0.1_1000_Nd100_Griewank_Dim50_100000.csv",sep=";")
dadosFull3<-read.csv("C:\\Users\\Jamisson\\workspace\\complexnetwork\\Results\\MyGA_Full_m0.01_1000_Nd100_Griewank_Dim50_100000.csv",sep=";")
dadosFull2<-read.csv("C:\\Users\\Jamisson\\workspace\\complexnetwork\\Results\\MyGA_Full_m0.001_1000_Nd100_Griewank_Dim50_100000.csv",sep=";")
dadosFull1<-read.csv("C:\\Users\\Jamisson\\workspace\\complexnetwork\\Results\\MyGA_Full_m0.0001_1000_Nd100_Griewank_Dim50_100000.csv",sep=";")
dadosFull0<-read.csv("C:\\Users\\Jamisson\\workspace\\complexnetwork\\Results\\MyGA_Full_m0.0_1000_Nd100_Griewank_Dim50_100000.csv",sep=";")

dFull4<-c(dadosFull4[,1])
dFull3<-c(dadosFull3[,1])
dFull2<-c(dadosFull2[,1])
dFull1<-c(dadosFull1[,1])
dFull0<-c(dadosFull0[,1])
boxplot(dFull4,dFull3,dFull2,dFull1, names=c("0.1","0.01","0.001","0.0001"),main="Problema Griewank",xlab="Coeficiente de Migração", ylab="Fitness")


//All Griewank
dadosFull<-read.csv("C:\\Users\\Jamisson\\workspace\\complexnetwork\\Results\\MyGA_Full_m0.1_1000_Nd100_Griewank_Dim50_100000.csv",sep=";")
dadosER<-read.csv("C:\\Users\\Jamisson\\workspace\\complexnetwork\\Results\\MyGA_ER_m0.1_1000_Nd100_Griewank_Dim50_100000.csv",sep=";")
dadosBA<-read.csv("C:\\Users\\Jamisson\\workspace\\complexnetwork\\Results\\MyGA_BA_m0.1_1000_Nd100_Griewank_Dim50_100000.csv",sep=";")
dadosWS<-read.csv("C:\\Users\\Jamisson\\workspace\\complexnetwork\\Results\\MyGA_WS_m0.1_1000_Nd100_Griewank_Dim50_100000.csv",sep=";")
dadosRing<-read.csv("C:\\Users\\Jamisson\\workspace\\complexnetwork\\Results\\MyGA_Ring_m0.1_1000_Nd100_Griewank_Dim50_100000.csv",sep=";")

seriesFull<-ts(dadosFull,names=c("Fitness","Average Fitness"))
seriesER<-ts(dadosER,names=c("Fitness","Average Fitness"))
seriesBA<-ts(dadosBA,names=c("Fitness","Average Fitness"))
seriesWS<-ts(dadosWS,names=c("Fitness","Average Fitness"))
seriesRing<-ts(dadosRing,names=c("Fitness","Average Fitness"))

FitFull<-c(dadosFull[,1])
FitER<-c(seriesER[,1])
FitBA<-c(seriesBA[,1])
FitWS<-c(seriesWS[,1])
FitRing<-c(seriesRing[,1])
boxplot(FitFull,FitER,FitBA,FitWS,FitRing, names=c("Global","ER","BA","WS","Anel"),main="Problema Griewank (m=0.1)",xlab="Modelos", ylab="Fitness")

AvgFitFull<-c(dadosFull[,2])
AvgFitER<-c(dadosER[,2])
AvgBA<-c(dadosBA[,2])
AvgFitWS<-c(dadosWS[,2])
AvgFitRing<-c(dadosRing[,2])
boxplot(AvgFitFull,AvgFitER,AvgBA,AvgFitWS,AvgFitRing, names=c("Global","ER","BA","WS","Anel"),main="Problema Griewank (m=0.1)",xlab="Modelos", ylab="Fitness médio")



//All Rosenbrock
dadosFull<-read.csv("C:\\Users\\Jamisson\\workspace\\complexnetwork\\Results\\MyGA_Full_m0.1_1000_Nd100_Rosenbrock_Dim50_100000.csv",sep=";")
dadosER<-read.csv("C:\\Users\\Jamisson\\workspace\\complexnetwork\\Results\\MyGA_ER_m0.1_1000_Nd100_Rosenbrock_Dim50_100000.csv",sep=";")
dadosBA<-read.csv("C:\\Users\\Jamisson\\workspace\\complexnetwork\\Results\\MyGA_BA_m0.1_1000_Nd100_Rosenbrock_Dim50_100000.csv",sep=";")
dadosWS<-read.csv("C:\\Users\\Jamisson\\workspace\\complexnetwork\\Results\\MyGA_WS_m0.1_1000_Nd100_Rosenbrock_Dim50_100000.csv",sep=";")
dadosRing<-read.csv("C:\\Users\\Jamisson\\workspace\\complexnetwork\\Results\\MyGA_Ring_m0.1_1000_Nd100_Rosenbrock_Dim50_100000.csv",sep=";")

seriesFull<-ts(dadosFull,names=c("Fitness","Average Fitness"))
seriesER<-ts(dadosER,names=c("Fitness","Average Fitness"))
seriesBA<-ts(dadosBA,names=c("Fitness","Average Fitness"))
seriesWS<-ts(dadosWS,names=c("Fitness","Average Fitness"))
seriesRing<-ts(dadosRing,names=c("Fitness","Average Fitness"))

FitFull<-c(dadosFull[,1])
FitER<-c(seriesER[,1])
FitBA<-c(seriesBA[,1])
FitWS<-c(seriesWS[,1])
FitRing<-c(seriesRing[,1])
boxplot(FitFull,FitER,FitBA,FitWS,FitRing, names=c("Global","ER","BA","WS","Anel"),main="Problema Rosenbrock(m=0.1)",xlab="Modelos", ylab="Fitness")



//All Sphere
dadosFull<-read.csv("C:\\Users\\Jamisson\\workspace\\complexnetwork\\Results\\MyGA_Full_m0.1_1000_Nd100_Sphere_Dim50_100000.csv",sep=";")
dadosER<-read.csv("C:\\Users\\Jamisson\\workspace\\complexnetwork\\Results\\MyGA_ER_m0.1_1000_Nd100_Sphere_Dim50_100000.csv",sep=";")
dadosBA<-read.csv("C:\\Users\\Jamisson\\workspace\\complexnetwork\\Results\\MyGA_BA_m0.1_1000_Nd100_Sphere_Dim50_100000.csv",sep=";")
dadosWS<-read.csv("C:\\Users\\Jamisson\\workspace\\complexnetwork\\Results\\MyGA_WS_m0.1_1000_Nd100_Sphere_Dim50_100000.csv",sep=";")
dadosRing<-read.csv("C:\\Users\\Jamisson\\workspace\\complexnetwork\\Results\\MyGA_Ring_m0.1_1000_Nd100_Sphere_Dim50_100000.csv",sep=";")

seriesFull<-ts(dadosFull,names=c("Fitness","Average Fitness"))
seriesER<-ts(dadosER,names=c("Fitness","Average Fitness"))
seriesBA<-ts(dadosBA,names=c("Fitness","Average Fitness"))
seriesWS<-ts(dadosWS,names=c("Fitness","Average Fitness"))
seriesRing<-ts(dadosRing,names=c("Fitness","Average Fitness"))

FitFull<-c(dadosFull[,1])
FitER<-c(seriesER[,1])
FitBA<-c(seriesBA[,1])
FitWS<-c(seriesWS[,1])
FitRing<-c(seriesRing[,1])
boxplot(FitFull,FitER,FitBA,FitWS,FitRing, names=c("Global","ER","BA","WS","Anel"),main="Problema Sphere(m=0.1)",xlab="Modelos", ylab="Fitness")



//All Rastrigin
dadosFull<-read.csv("C:\\Users\\Jamisson\\workspace\\complexnetwork\\Results\\MyGA_Full_m0.1_1000_Nd100_Rastrigin_Dim50_100000.csv",sep=";")
dadosER<-read.csv("C:\\Users\\Jamisson\\workspace\\complexnetwork\\Results\\MyGA_ER_m0.1_1000_Nd100_Rastrigin_Dim50_100000.csv",sep=";")
dadosBA<-read.csv("C:\\Users\\Jamisson\\workspace\\complexnetwork\\Results\\MyGA_BA_m0.1_1000_Nd100_Rastrigin_Dim50_100000.csv",sep=";")
dadosWS<-read.csv("C:\\Users\\Jamisson\\workspace\\complexnetwork\\Results\\MyGA_WS_m0.1_1000_Nd100_Rastrigin_Dim50_100000.csv",sep=";")
dadosRing<-read.csv("C:\\Users\\Jamisson\\workspace\\complexnetwork\\Results\\MyGA_Ring_m0.1_1000_Nd100_Rastrigin_Dim50_100000.csv",sep=";")

seriesFull<-ts(dadosFull,names=c("Fitness","Average Fitness"))
seriesER<-ts(dadosER,names=c("Fitness","Average Fitness"))
seriesBA<-ts(dadosBA,names=c("Fitness","Average Fitness"))
seriesWS<-ts(dadosWS,names=c("Fitness","Average Fitness"))
seriesRing<-ts(dadosRing,names=c("Fitness","Average Fitness"))

FitFull<-c(dadosFull[,1])
FitER<-c(seriesER[,1])
FitBA<-c(seriesBA[,1])
FitWS<-c(seriesWS[,1])
FitRing<-c(seriesRing[,1])
boxplot(FitFull,FitER,FitBA,FitWS,FitRing, names=c("Global","ER","BA","WS","Anel"),main="Problema Rastrigin(m=0.1)",xlab="Modelos", ylab="Fitness")




shapiro.test(HVnsgaII)
shapiro.test(HVneuroGA)
var.test(HVnsgaII,HVneuroGA)
t.test(HVnsgaII,HVneuroGA,var.equal=TRUE,alternative="two.sided")
shapiro.test(SpreadNsgaII)
shapiro.test(SpreadNeuroGA)
wilcox.test(SpreadNsgaII,SpreadNeuroGA)

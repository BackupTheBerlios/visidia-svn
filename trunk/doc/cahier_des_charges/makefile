#LEX
LEX=pdflatex
LEXOPTIONS=-interaction nonstopmode

#DIA
DIA=dia
DIAOPTIONS=-e

EPSTOPDF=epstopdf
PSTOPDF=ps2pdf
BIBTEX=bibtex

#FIGURES
FIGURESDIR= figures
FIGURESPDF= classes.pdf simple.pdf UseCase.pdf seqDeplacement.pdf seqProperties.pdf \
            gantt.pdf output.pdf
#TEX
SOURCES=main.tex introduction.tex modele_systeme.tex besoins.tex \
	organisation.tex lexique.tex biblio.tex desc_choisirAgent.tex \
        desc_choisirGraphe.tex desc_RegleEcriture.tex desc_ParamSimu.tex desc_lancerSimulation.tex

all: main.pdf

clean:
	rm -f *.pdf *.toc *.aux *.out *.log *.bbl *.blg *.eps *.ps

send: main.pdf
	scp main.pdf cassou@shell.berlios.de:/home/groups/visidia/htdocs/cahier_des_charges.pdf

main.pdf: main.aux $(SOURCES) main.bbl $(FIGURESPDF)
	$(LEX) $(LEXOPTIONS) main || echo
	$(LEX) $(LEXOPTIONS) main || echo

main.aux: $(SOURCES) $(FIGURESPDF)
	$(LEX) $(LEXOPTIONS) main || echo

main.bbl: $(SOURCES) myBib.bib
	$(BIBTEX) main

$(FIGURESDIR)/simple.eps: $(FIGURESDIR)/simple.dia
	$(DIA) $(FIGURESDIR)/simple.dia $(DIAOPTIONS) $(FIGURESDIR)/simple.eps

simple.eps: $(FIGURESDIR)/simple.eps
	cp $(FIGURESDIR)/simple.eps .

simple.pdf: simple.eps
	$(EPSTOPDF) simple.eps

$(FIGURESDIR)/UseCase.eps: $(FIGURESDIR)/UseCase.dia
	$(DIA) $(FIGURESDIR)/UseCase.dia $(DIAOPTIONS) $(FIGURESDIR)/UseCase.eps

UseCase.eps: $(FIGURESDIR)/UseCase.eps
	cp $(FIGURESDIR)/UseCase.eps .

UseCase.pdf: UseCase.eps
	$(EPSTOPDF) UseCase.eps

$(FIGURESDIR)/classes.eps: $(FIGURESDIR)/classes.dia
	$(DIA) $(FIGURESDIR)/classes.dia $(DIAOPTIONS) $(FIGURESDIR)/classes.eps

classes.eps: $(FIGURESDIR)/classes.eps
	cp figures/classes.eps .

classes.pdf: classes.eps
	$(EPSTOPDF) classes.eps

$(FIGURESDIR)/seqDeplacement.eps: $(FIGURESDIR)/seqDeplacement.dia
	$(DIA) $(FIGURESDIR)/seqDeplacement.dia $(DIAOPTIONS) $(FIGURESDIR)/seqDeplacement.eps

seqDeplacement.eps: $(FIGURESDIR)/seqDeplacement.eps
	cp figures/seqDeplacement.eps .

seqDeplacement.pdf: seqDeplacement.eps
	$(EPSTOPDF) seqDeplacement.eps

$(FIGURESDIR)/seqProperties.eps: $(FIGURESDIR)/seqProperties.dia
	$(DIA) $(FIGURESDIR)/seqProperties.dia $(DIAOPTIONS) $(FIGURESDIR)/seqProperties.eps

seqProperties.eps: $(FIGURESDIR)/seqProperties.eps
	cp figures/seqProperties.eps .

seqProperties.pdf: seqProperties.eps
	$(EPSTOPDF) seqProperties.eps

gantt.ps: $(FIGURESDIR)/gantt.ps
	cp figures/gantt.ps .

gantt.pdf: gantt.ps
	$(PSTOPDF) gantt.ps

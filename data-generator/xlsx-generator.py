import xlsxwriter
import datetime
import random

class antibiotic:
    def __init__(self, name, code):
        self.name = name
        self.code = code

antibiotics = [antibiotic("Amikacin", "AN"),
    antibiotic("Ciprofloxacin", "CIP"),
    antibiotic("Clindamycin", "CC"),
    antibiotic("Daptomycin", "DAP"),
    antibiotic("Erythromycin", "E"),
    antibiotic("Gentamicin", "GE"),
    antibiotic("Levofloxacin", "LEV"),
    antibiotic("Linezolid", "LIN"),
    antibiotic("Oxacillin", "OX"),
    antibiotic("Rifampicin", "RIF"),
    antibiotic("Teicoplanin", "TEC"),
    antibiotic("A1", "A1"),
    antibiotic("A2", "A2"),
    antibiotic("A3", "A3"),
    antibiotic("A4", "A4"),
    antibiotic("A5", "A5"),
    antibiotic("A6", "A6")]

class bacteria:
    def __init__(self, name, subtype):
        self.name = name
        self.subtype = subtype

bacterias = [bacteria("Staphylococcus aureus", ""),
    bacteria("Staphylococcus epidermidis", "MRCNS"),
    bacteria("Staphylococcus cohnii ssp urealyticus", ""),
    bacteria("Enterococcus faecalis", "HLGR"),
    bacteria("Enterococcus faecium", "VRE "),
    bacteria("Actinomyces naeslundii", ""),
    bacteria("b1", ""),
    bacteria("b2", ""),
    bacteria("b3", ""),
    bacteria("b4", ""),
    bacteria("b5", "")]

class material:
    def __init__(self, name):
        self.name = name

materials = [material("Wymaz z ropnia - posiew"),
    material("Wymaz z nosa w kierunku MRSA - posiew"),
    material("Mocz"),
    material("M1"),
    material("M2"),
    material("M3"),
    material("M4"),
    material("M5"),
    material("M6"),
    material("M7"),
    material("M8")]

class patient:
    def __init__(self, pesel, first_name, second_name):
        self.pesel = pesel
        self.first_name = first_name
        self.second_name = second_name

patients = [patient(97041012345, "Jan", "Kowalski"),
    patient(96012234567, "Joanna", "Nowak"),
    patient(23847623453, "Wiktor", "Wozny"),
    patient(23847623453, "Wiktor", "Wozny"),
    patient(23847623453, "Wiktor", "Wozny"),
    patient(23847623453, "Wiktor", "Wozny"),
    patient(23847623453, "Wiktor", "Wozny"),]

class ward:
    def __init__(self, name):
        self.name = name

wards = [ward("Chirurgia")]

class examination:
    def __init__(self, number, material, patient, ward):
        self.number = number
        self.material = material
        self.patient = patient
        self.ward = ward

class examination:
    def __init__(self, order_number, order_date, material, patient, ward):
        self.number = order_number
        self.order_date = order_date
        self.material = material
        self.patient = patient
        self.ward = ward

class antibiogram:
    def __init__(self, order_date, order_number, result, suspectability, 
    antibiotic, bacteria, examination, material, patient, ward):
        self.alert = 0
        self.first_isolate = 0
        self.growth = ""
        self.hospital_infection = 0
        self.isolation_code = ""
        self.isolation_id = 69
        self.isolation_num = 420
        self.mic = ""
        self.mode = "R"
        self.order_date = order_date
        self.order_id = 1337
        self.order_number = order_number  # used in Examination
        self.patogen = 0
        self.pryw = ""
        self.result = result
        self.suspectability = suspectability
        self.test_id = 1234
        self.daily_num = "042.0128"

        self.antibiotic = antibiotic
        self.bacteria = bacteria
        self.examination = examination
        self.material = material
        self.patient = patient
        self.ward = ward

class generator:
    def __init__(self, num=0):
        self.num = num
    def next_num(self):
        self.num += 1
        return self.num - 1

def get_random_material():
    return materials[random.randint(0, len(materials)-1)]

def get_random_patient():
    return patients[random.randint(0, len(patients)-1)]

def get_random_indeces(lis):
    l = len(lis)
    return random.sample(range(0, l), random.randint(1, l))

material_bacterias = dict()

def fill_material_bacterias():
    for m in materials:
        #https://www.kite.com/python/answers/how-to-access-multiple-indices-of-a-list-in-python
        bacts = list( map(bacterias.__getitem__, get_random_indeces(bacterias)))
        material_bacterias[m] = bacts
fill_material_bacterias()

bacteria_antibiotic = dict()
def fill_bacteria_antibiotic():
    for b in bacterias:
        anti = list( map(antibiotics.__getitem__, get_random_indeces(antibiotics)) )
        bacteria_antibiotic[b] = anti
fill_bacteria_antibiotic()

def generate_antibiograms(order_date, order_number, examination, 
    material, patient, ward):
    # generate result, suspectability, antibiotic, bacteria
    # bacteria -> some random bacteria from material_bacterias
    # antibiotic -> 1:1 set of antibiotics from bacterias (bacteria_antibiotic)
    antibiograms = []
    
    # material
    # print(material_bacterias[material])
    for i in get_random_indeces(material_bacterias[material]):
        bact = material_bacterias[material][i]
        for j in range(0, len(bacteria_antibiotic[bact])):
            anti = bacteria_antibiotic[bact][j]
            suspect = random.randint(0, 2)
            suspectibility = "S"
            result = "wrażliwy"
            if suspect == 1:
                suspectibility = "R"
                result = "oporny"
            elif suspect == 2:
                suspectibility = "I"
                result = "średniowrażliwy"
            single_anitibiogram = antibiogram(order_date, order_number, result, suspectibility, anti, bact, examination, material, patient, ward)
            antibiograms.append(single_anitibiogram)
    return antibiograms

def getRandomDate(yearA, yearB):
    if yearA >= yearB:
        yearA = 2017
        yearB = 2022

    start_date = datetime.date(yearA, 1, 1)
    end_date = datetime.date(yearB, 1, 1)
    time_between_dates = end_date - start_date
    days_between_dates = time_between_dates.days
    random_number_of_days = random.randrange(days_between_dates)

    return start_date + datetime.timedelta(days=random_number_of_days)

def generate_examination(order_number):
    #order_number_generator = generator()
    #t = datetime.datetime(2012, 2, 23, 0, 0)
    t = getRandomDate(2017, 2022)
    order_date = t.strftime('%m/%d/%Y')
    material = get_random_material()
    patient = get_random_patient()
    ward = wards[0]
    exam = examination(order_number,
        order_date,
        material,
        patient,
        ward)
    return generate_antibiograms(order_date, order_number, exam, material, patient, ward)

for a in antibiotics:
    print(a.name, a.code)

# print(bacteria_antibiotic)
# print()
# print(material_bacterias)
# print("code")

row = 0
col = 0

# Create a workbook and add worksheets
workbook = xlsxwriter.Workbook('different_dates.xlsx')
worksheet0 = workbook.add_worksheet()
worksheet1 = workbook.add_worksheet()
worksheet_FRAT = workbook.add_worksheet()
worksheet_FRAT.write(row, col, "Zleceniodawca")
worksheet_FRAT.write(row, col+1, "Pacjent")
worksheet_FRAT.write(row, col+2, "")
worksheet_FRAT.write(row, col+3, "PESEL/Data ur.")
worksheet_FRAT.write(row, col+4, "Data zlecenia")
worksheet_FRAT.write(row, col+5, "Nr zlecenie")
worksheet_FRAT.write(row, col+6, "Materiał badany")
worksheet_FRAT.write(row, col+7, "Izolacja")
worksheet_FRAT.write(row, col+8, "Mechanizmy opornoci")
worksheet_FRAT.write(row, col+9, "Antybiotyk")
worksheet_FRAT.write(row, col+10, "Wr")
worksheet_FRAT.write(row, col+11, "Mic")
worksheet_FRAT.write(row, col+12, "Alert")
worksheet_FRAT.write(row, col+13, "Patogen")
worksheet_FRAT.write(row, col+14, "Wzrost")
worksheet_FRAT.write(row, col+15, "Pierwszy~izolat")
worksheet_FRAT.write(row, col+16, "Zakażenie~szpitalne")
worksheet_FRAT.write(row, col+17, "ID Zlecenia")
worksheet_FRAT.write(row, col+18, "ID Badania")
worksheet_FRAT.write(row, col+19, "ID Izolacji")
worksheet_FRAT.write(row, col+20, "Kod izolacji")
worksheet_FRAT.write(row, col+21, "IzolNum")
worksheet_FRAT.write(row, col+22, "Kod antybiotyku")
worksheet_FRAT.write(row, col+23, "Wynik")
worksheet_FRAT.write(row, col+24, "Nr dzienny")
worksheet_FRAT.write(row, col+25, "Tryb")
worksheet_FRAT.write(row, col+26, "Pryw")

row = 1
order_number_generator = generator()
for i in range(0, 600):
    for anti in generate_examination(order_number_generator.next_num()):
        worksheet_FRAT.write(row, col, anti.ward.name)
        worksheet_FRAT.write(row, col+1, anti.patient.first_name)
        worksheet_FRAT.write(row, col+2, anti.patient.second_name)
        worksheet_FRAT.write(row, col+3, anti.patient.pesel)
        worksheet_FRAT.write(row, col+4, anti.order_date)
        worksheet_FRAT.write(row, col+5, anti.order_number)
        worksheet_FRAT.write(row, col+6, anti.material.name)
        worksheet_FRAT.write(row, col+7, anti.bacteria.name)
        worksheet_FRAT.write(row, col+8, anti.bacteria.subtype)
        worksheet_FRAT.write(row, col+9, anti.antibiotic.name)
        worksheet_FRAT.write(row, col+10, anti.suspectability)
        worksheet_FRAT.write(row, col+11, anti.mic)
        worksheet_FRAT.write(row, col+12, anti.alert)
        worksheet_FRAT.write(row, col+13, anti.patogen)
        worksheet_FRAT.write(row, col+14, anti.growth)
        worksheet_FRAT.write(row, col+15, anti.first_isolate)
        worksheet_FRAT.write(row, col+16, anti.hospital_infection)
        worksheet_FRAT.write(row, col+17, anti.order_id)
        worksheet_FRAT.write(row, col+18, anti.test_id)
        worksheet_FRAT.write(row, col+19, anti.isolation_id)
        worksheet_FRAT.write(row, col+20, anti.isolation_code)
        worksheet_FRAT.write(row, col+21, anti.isolation_num)
        worksheet_FRAT.write(row, col+22, anti.antibiotic.code)
        worksheet_FRAT.write(row, col+23, anti.result)
        worksheet_FRAT.write(row, col+24, anti.daily_num)
        worksheet_FRAT.write(row, col+25, anti.mode)
        worksheet_FRAT.write(row, col+26, anti.pryw)
        row += 1
    
workbook.close()

print("done :)")
